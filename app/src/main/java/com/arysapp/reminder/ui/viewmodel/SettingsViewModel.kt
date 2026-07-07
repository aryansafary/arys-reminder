package com.arysapp.reminder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.reminder.domain.usecase.BackupReminderUseCase
import com.arysapp.reminder.domain.usecase.RestoreRemindersUseCase
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
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupReminderUseCase: BackupReminderUseCase,
    private val restoreRemindersUseCase: RestoreRemindersUseCase
) : ViewModel() {

    private val _backupUiState = MutableStateFlow<BackupUiState>(BackupUiState.Idle)
    val backupUiState: StateFlow<BackupUiState> = _backupUiState.asStateFlow()

    private val _restoreUiState = MutableStateFlow<RestoreUiState>(RestoreUiState.Idle)
    val restoreUiState: StateFlow<RestoreUiState> = _restoreUiState.asStateFlow()

    private val _events = Channel<ShowSnackBar>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun getBackupDataForExport(onDataReady: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _backupUiState.value = BackupUiState.Loading

            val result = backupReminderUseCase()

            if (result.isSuccess) {
                val jsonContent = result.getOrThrow()
                _backupUiState.value = BackupUiState.Success("data ready to export")
                onDataReady(jsonContent)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "error in backup"
                _backupUiState.value = BackupUiState.Error(errorMsg)
                _events.send(ShowSnackBar.ShowSnack(errorMsg, true))
            }
        }
    }

    fun performRestoreFromJson(jsonContent: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _restoreUiState.value = RestoreUiState.Loading

            val result = restoreRemindersUseCase(jsonContent)

            if (result.isSuccess) {
                val restoredCount = result.getOrDefault(0)
                _restoreUiState.value = RestoreUiState.Success(restoredCount.toString())
                _events.send(ShowSnackBar.ShowSnack("restore $restoredCount reminder successfully"))
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "error in restore"
                _restoreUiState.value = RestoreUiState.Error(errorMsg)
                _events.send(ShowSnackBar.ShowSnack(errorMsg, true))
            }
        }
    }
}