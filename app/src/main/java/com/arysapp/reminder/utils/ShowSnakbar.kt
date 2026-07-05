package com.arysapp.reminder.utils


sealed class ShowSnackBar {
    data class ShowSnack(val message: String, val isError: Boolean = false) : ShowSnackBar()

}
