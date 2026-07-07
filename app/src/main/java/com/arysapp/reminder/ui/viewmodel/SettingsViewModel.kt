package com.arysapp.reminder.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.reminder.domain.model.BackupFileModel
import com.arysapp.reminder.domain.usecase.ReminderUseCases
import com.arysapp.reminder.utils.BackupUiState
import com.arysapp.reminder.utils.RestoreUiState
import com.arysapp.reminder.utils.ShowSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val reminderUseCases: ReminderUseCases
) : ViewModel() {


    private val _backupUiState =
        MutableStateFlow<BackupUiState>(BackupUiState.Idle)

    val backupUiState: StateFlow<BackupUiState> =
        _backupUiState.asStateFlow()


    private val _restoreUiState =
        MutableStateFlow<RestoreUiState>(RestoreUiState.Idle)

    val restoreUiState: StateFlow<RestoreUiState> =
        _restoreUiState.asStateFlow()


    private val _backupFiles =
        MutableStateFlow<List<BackupFileModel>>(emptyList())

    val backupFiles: StateFlow<List<BackupFileModel>> =
        _backupFiles.asStateFlow()


    private val _events =
        Channel<ShowSnackBar>(Channel.BUFFERED)

    val events = _events.receiveAsFlow()



    fun performBackupToDownloads(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {

            _backupUiState.value = BackupUiState.Loading

            try {

                val result =
                    reminderUseCases.backupReminders()


                if (result.isFailure) {

                    showError(
                        context.getString(
                            com.arysapp.reminder.R.string.not_found_reminder
                        )
                    )

                    return@launch
                }


                val jsonContent = result.getOrThrow()

                val fileName =
                    "ArysReminder-Backup_${System.currentTimeMillis()}.json"



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {


                    val values = ContentValues().apply {

                        put(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            fileName
                        )

                        put(
                            MediaStore.MediaColumns.MIME_TYPE,
                            "application/json"
                        )

                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            "${Environment.DIRECTORY_DOWNLOADS}/Arys"
                        )
                    }


                    val uri =
                        context.contentResolver.insert(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                            values
                        )
                            ?: throw Exception()



                    context.contentResolver
                        .openOutputStream(uri)
                        ?.use {
                            it.write(jsonContent.toByteArray())
                        }


                } else {


                    @Suppress("DEPRECATION")
                    val folder = File(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS
                        ),
                        "Arys"
                    )


                    if (!folder.exists())
                        folder.mkdirs()


                    File(folder, fileName)
                        .writeText(jsonContent)

                }



                withContext(Dispatchers.Main) {

                    _backupUiState.value =
                        BackupUiState.Success(fileName)


                    _events.send(
                        ShowSnackBar.ShowSnack(
                            context.getString(
                                com.arysapp.reminder.R.string.backup_success
                            )
                        )
                    )
                }


            } catch (e: Exception) {

                showError(
                    e.message
                        ?: context.getString(
                            com.arysapp.reminder.R.string.error_backup
                        )
                )
            }
        }
    }




    fun loadBackupFiles() {

        viewModelScope.launch(Dispatchers.IO) {

            _backupFiles.value =
                reminderUseCases.getBackupFiles()

        }
    }





    fun performRestoreFromFile(
        context: Context,
        backupFile: File,
        onSuccess: () -> Unit = {}
    ) {


        viewModelScope.launch(Dispatchers.IO) {


            _restoreUiState.value =
                RestoreUiState.Loading


            try {


                backupFile.inputStream()
                    .use { input ->


                        val result =
                            reminderUseCases.restoreReminders(input)


                        if (result.isFailure)
                            throw result.exceptionOrNull()
                                ?: Exception()



                        withContext(Dispatchers.Main) {


                            _restoreUiState.value =
                                RestoreUiState.Success(
                                    result.getOrDefault(0)
                                        .toString()
                                )


                            _events.send(
                                ShowSnackBar.ShowSnack(
                                    context.getString(
                                        com.arysapp.reminder.R.string.restore_success
                                    )
                                )
                            )


                            onSuccess()
                        }

                    }



            } catch (e: Exception) {


                withContext(Dispatchers.Main) {

                    _restoreUiState.value =
                        RestoreUiState.Error(
                            e.message ?: ""
                        )


                    _events.send(
                        ShowSnackBar.ShowSnack(
                            e.message ?: "",
                            true
                        )
                    )
                }

            }

        }

    }



    private suspend fun showError(message: String) {

        withContext(Dispatchers.Main) {

            _backupUiState.value =
                BackupUiState.Error(message)


            _events.send(
                ShowSnackBar.ShowSnack(
                    message,
                    true
                )
            )
        }
    }

}