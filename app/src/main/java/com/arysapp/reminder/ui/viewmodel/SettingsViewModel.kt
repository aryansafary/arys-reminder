package com.arysapp.reminder.ui.viewmodel

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.usecase.ReminderUseCases
import com.arysapp.reminder.utils.BackupUiState
import com.arysapp.reminder.utils.RestoreUiState
import com.arysapp.reminder.utils.ShowSnackBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import kotlin.text.Charsets
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val reminderUseCases: ReminderUseCases
) : ViewModel() {

    // States
    private val _backupUiState = MutableStateFlow<BackupUiState>(BackupUiState.Idle)
    val backupUiState: StateFlow<BackupUiState> = _backupUiState.asStateFlow()

    private val _restoreUiState = MutableStateFlow<RestoreUiState>(RestoreUiState.Idle)
    val restoreUiState: StateFlow<RestoreUiState> = _restoreUiState.asStateFlow()


    private val _events = Channel<ShowSnackBar>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    fun performBackupToDownloads(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _backupUiState.value = BackupUiState.Loading
            try {
                val tasks = reminderUseCases.getAllReminders().first()
                if (tasks.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        _backupUiState.value = BackupUiState.Error(context.getString(com.arysapp.reminder.R.string.empty_backup_task))
                        _events.send(ShowSnackBar.ShowSnack(context.getString(com.arysapp.reminder.R.string.not_found_reminder), isError = true))
                    }
                    return@launch
                }
                val gson = Gson()
                val jsonContent = gson.toJson(tasks)
                val fileName = "ArysReminder-Backup_${System.currentTimeMillis()}.json"

                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/Arys")
                    }
                }
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                uri?.let { insertUri ->
                    context.contentResolver.openOutputStream(insertUri)?.use { outputStream ->
                        outputStream.write(jsonContent.toByteArray())
                    }
                    withContext(Dispatchers.Main) {
                        _backupUiState.value = BackupUiState.Success("Arys/Reminder/$fileName")
                        _events.send(ShowSnackBar.ShowSnack(context.getString(com.arysapp.reminder.R.string.backup_success)))
                    }
                } ?: run {
                    withContext(Dispatchers.Main) {
                        _backupUiState.value = BackupUiState.Error(context.getString(com.arysapp.reminder.R.string.error_save_file))
                        _events.send(ShowSnackBar.ShowSnack(context.getString(com.arysapp.reminder.R.string.error_save), isError = true))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _backupUiState.value = BackupUiState.Error(e.message ?: context.getString(com.arysapp.reminder.R.string.error_backup))
                    _events.send(ShowSnackBar.ShowSnack("${context.getString(com.arysapp.reminder.R.string.error)} : ${e.message}", isError = true))
                }
            }
        }
    }


    fun performRestore(context:Context ,restoreUri: Uri, contentResolver: ContentResolver, onSuccess: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            _restoreUiState.value = RestoreUiState.Loading
            try {
                val json = contentResolver.openInputStream(restoreUri)?.use { inputStream ->
                    inputStream.readBytes().toString(Charsets.UTF_8)
                } ?: throw IllegalStateException(context.getString(com.arysapp.reminder.R.string.can_not_read_file))

                val gson = Gson()
                val type = object : TypeToken<List<ReminderModel>>() {}.type
                val reminders: List<ReminderModel> = gson.fromJson(json, type) ?: emptyList()

                if (reminders.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        _restoreUiState.value = RestoreUiState.Error(context.getString(com.arysapp.reminder.R.string.invalid_file))
                        _events.send(ShowSnackBar.ShowSnack(context.getString(com.arysapp.reminder.R.string.empty_file), isError = true))
                    }
                    return@launch
                }

                // پاک کردن تسک‌های فعلی
                val currentReminders = reminderUseCases.getAllReminders().first()
                currentReminders.forEach { currentTask ->
                    reminderUseCases.deleteReminder(currentTask)
                }

                val insertedCount = reminders.map { reminder ->
                    reminderUseCases.insertReminder(
                        title = reminder.title,
                        description = reminder.description,
                        dateTime = reminder.dateTime,
                        hourTime = reminder.hourTime,
                        repeatType = reminder.repeatType,
                        repeatIntervalDays = reminder.repeatIntervalDays,
                        repeatIntervalWeeks = reminder.repeatIntervalWeeks,
                        repeatIntervalMonths = reminder.repeatIntervalMonths,
                        reminderMinutesBefore = reminder.reminderMinutesBefore
                    ).getOrNull() ?: 0L
                }.count { it > 0 }

                withContext(Dispatchers.Main) {
                    _restoreUiState.value = RestoreUiState.Success("${context.getString(com.arysapp.reminder.R.string.count_insert)} : $insertedCount")
                    _events.send(ShowSnackBar.ShowSnack(context.getString(com.arysapp.reminder.R.string.restore_success)))
                    onSuccess() // Refresh tasks
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _restoreUiState.value = RestoreUiState.Error(e.message ?: context.getString(com.arysapp.reminder.R.string.error_restore))
                    _events.send(ShowSnackBar.ShowSnack("خطا: ${e.message}", isError = true))
                }
            }
        }
    }
}








