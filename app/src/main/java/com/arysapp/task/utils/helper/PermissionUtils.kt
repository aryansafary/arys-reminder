package com.arysapp.task.utils.helper

import android.Manifest
import android.os.Build



object PermissionUtils {
    fun permissionList(): List<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                Manifest.permission.POST_NOTIFICATIONS,
//                Manifest.permission.READ_MEDIA_VIDEO,
//                Manifest.permission.READ_SMS,
//                Manifest.permission.RECEIVE_SMS,
//                Manifest.permission.SEND_SMS
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> listOf(
                Manifest.permission.SCHEDULE_EXACT_ALARM,
//                Manifest.permission.READ_MEDIA_VIDEO,
//                Manifest.permission.READ_SMS,
//                Manifest.permission.RECEIVE_SMS,
//                Manifest.permission.SEND_SMS
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> listOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_SMS,
//                Manifest.permission.RECEIVE_SMS,
//                Manifest.permission.SEND_SMS
            )
            else -> listOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_SMS,
//                Manifest.permission.RECEIVE_SMS,
//                Manifest.permission.SEND_SMS
            )
        }
    }
}
