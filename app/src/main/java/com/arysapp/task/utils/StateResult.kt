package com.arysapp.task.utils

sealed class StateResult<out T> {
    object Loading : StateResult<Nothing>()
    data class Success<T>(val data: T) : StateResult<T>()
    data class Error(val message: String) : StateResult<Nothing>()
}
