package com.arysapp.reminder.utils

sealed class BackupUiState {
    object Idle : BackupUiState()
    object Loading : BackupUiState()
    data class Success(val fileName: String) : BackupUiState()
    data class Error(val message: String) : BackupUiState()
}