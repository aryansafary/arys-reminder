package com.arysapp.task.utils


sealed class ShowSnackBar {
    data class ShowSnack(val message: String, val isError: Boolean = false) : ShowSnackBar()

}
