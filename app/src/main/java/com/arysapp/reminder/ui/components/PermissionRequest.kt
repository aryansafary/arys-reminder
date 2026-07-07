package com.arysapp.reminder.ui.components

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.arysapp.reminder.utils.helper.PermissionUtils

@Composable
fun PermissionRequest(onPermissionsGranted: () -> Unit) {
    val context = LocalContext.current
    val permissions = PermissionUtils.permissionList()

    var hasAllPermissions by remember { mutableStateOf(false) }

    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsResult ->
            hasAllPermissions = permissionsResult.values.all { it }
        }
    )

    LaunchedEffect(Unit) {
        val allGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) {
            hasAllPermissions = true
        } else {
            multiplePermissionsLauncher.launch(permissions.toTypedArray())
        }
    }

    if (hasAllPermissions) {
        onPermissionsGranted()
//    } else {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text("need permission")
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = {
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                    data = Uri.fromParts("package", context.packageName, null)
//                }
//                context.startActivity(intent)
//            }) {
//                Text("Grant Permissions")
//            }
//        }
    }
}