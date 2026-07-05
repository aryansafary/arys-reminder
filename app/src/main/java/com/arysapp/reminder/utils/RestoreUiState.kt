package com.arysapp.reminder.utils

sealed class RestoreUiState {
    object Idle : RestoreUiState()
    object Loading : RestoreUiState()
    data class Success(val message: String) : RestoreUiState()
    data class Error(val message: String) : RestoreUiState()
}