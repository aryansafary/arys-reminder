package com.arysapp.reminder.utils.helper

import android.Manifest
import android.os.Build



object PermissionUtils {
    fun permissionList(): List<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                Manifest.permission.POST_NOTIFICATIONS,

                )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
            else -> listOf(

            )

        }
    }
}
