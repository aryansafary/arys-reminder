package com.arysapp.reminder.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arysapp.reminder.R
import com.arysapp.reminder.ui.viewmodel.DataStoreViewmodel
import com.arysapp.reminder.ui.viewmodel.SettingsViewModel
import com.arysapp.reminder.ui.viewmodel.ReminderViewModel
import com.arysapp.reminder.utils.BackupUiState
import com.arysapp.reminder.utils.Constants.ENGLISH_LANGUAGE
import com.arysapp.reminder.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import com.arysapp.reminder.utils.RestoreUiState
import com.arysapp.reminder.utils.ShowSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen(
    dataStoreViewmodel: DataStoreViewmodel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    reminderViewModel: ReminderViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }

    val languages = listOf(
        "fa" to stringResource(R.string.persian_lang),
        "en" to stringResource(R.string.english_lang)
    )

    val rotateAnim by animateFloatAsState(
        targetValue = if (expanded) -90f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "rotateAnim"
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val backupState by settingsViewModel.backupUiState.collectAsState()
    val restoreState by settingsViewModel.restoreUiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    var showBackupDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }

    var pendingBackupData by remember { mutableStateOf<String?>(null) }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let { destinationUri ->
            scope.launch(Dispatchers.IO) {
                try {
                    context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                        pendingBackupData?.let { data ->
                            outputStream.write(data.toByteArray())
                        }
                    }
                    withContext(Dispatchers.Main) {
                        snackBarHostState.showSnackbar(
                            context.getString(R.string.backup_success), // پیام موفقیت به منابع string اضافه شود
                            duration = SnackbarDuration.Short
                        )
                    }
                } catch (_: Exception) {
                    withContext(Dispatchers.Main) {
                        snackBarHostState.showSnackbar(context.getString(R.string.error_save_file), duration = SnackbarDuration.Short)
                    }
                }
            }
        }
    }


    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { sourceUri ->
            scope.launch(Dispatchers.IO) {
                try {
                    val jsonContent = context.contentResolver.openInputStream(sourceUri)?.bufferedReader().use { reader ->
                        reader?.readText()
                    }

                    if (!jsonContent.isNullOrBlank()) {
                        withContext(Dispatchers.Main) {
                            settingsViewModel.performRestoreFromJson(jsonContent)
                            reminderViewModel.getAllReminders()
                        }
                    }
                } catch (_: Exception) {
                    withContext(Dispatchers.Main) {
                        snackBarHostState.showSnackbar(context.getString(R.string.can_not_read_file), duration = SnackbarDuration.Short)
                    }
                }
            }
        }
    }

    LaunchedEffect(settingsViewModel) {
        settingsViewModel.events.collect { event ->
            when (event) {
                is ShowSnackBar.ShowSnack -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ListItem(
            headlineContent = { Text(stringResource(R.string.language)) },
            supportingContent = {
                Text(
                    if (USER_LANGUAGE == PERSIAN_LANGUAGE) stringResource(R.string.persian_lang)
                    else stringResource(R.string.english_lang)
                )
            },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.icon_language_100px_2),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingContent = {
                Box {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            painter = painterResource(
                                if (USER_LANGUAGE == ENGLISH_LANGUAGE) R.drawable.icon_arrow_100px
                                else R.drawable.icon_arrow_100px_2
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(rotateAnim)
                        )
                    }
                }
            }
        )

        if (expanded) {
            HorizontalDivider()
            Text(
                text = stringResource(R.string.select_lang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.Start
            ) {
                languages.forEach { (code, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RadioButton(
                            selected = code == USER_LANGUAGE,
                            onClick = {
                                expanded = false
                                dataStoreViewmodel.setLanguage(if (code == PERSIAN_LANGUAGE) PERSIAN_LANGUAGE else ENGLISH_LANGUAGE)
                                restartActivity(context = context)
                            }
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        HorizontalDivider()

        ListItem(
            headlineContent = { Text(stringResource(R.string.back_up)) },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.icon_backup_100px),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier.clickable { showBackupDialog = true }
        )

        AnimatedVisibility(visible = showBackupDialog, enter = fadeIn()) {
            AlertDialog(
                onDismissRequest = { showBackupDialog = false },
                title = { Text(stringResource(R.string.backup)) },
                text = { Text(stringResource(R.string.backup_text)) },
                confirmButton = {
                    Button(onClick = {
                        showBackupDialog = false
                        settingsViewModel.getBackupDataForExport { jsonData ->
                            scope.launch(Dispatchers.Main) {
                                pendingBackupData = jsonData
                                val fileName = "ArysReminder-Backup_${System.currentTimeMillis()}.json"
                                createDocumentLauncher.launch(fileName)
                            }
                        }
                    }) {
                        Text(stringResource(R.string.start_backup))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showBackupDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

        HorizontalDivider()

        ListItem(
            headlineContent = { Text(stringResource(R.string.restore)) },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.icon_restore_100px),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier.clickable { showRestoreDialog = true }
        )

        AnimatedVisibility(visible = showRestoreDialog, enter = fadeIn()) {
            AlertDialog(
                onDismissRequest = { showRestoreDialog = false },
                title = { Text(stringResource(R.string.restore)) },
                text = {
                    Text(stringResource(R.string.restore_text))
                },
                confirmButton = {
                    Button(onClick = {
                        showRestoreDialog = false

                        openDocumentLauncher.launch(arrayOf("application/json"))
                    }) {
                        Text(stringResource(R.string.restore))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRestoreDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

        HorizontalDivider()

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    if (backupState is BackupUiState.Loading || restoreState is RestoreUiState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

fun restartActivity(context: Context) {
    val activity = context as? Activity ?: return
    activity.finish()
    activity.startActivity(Intent(activity, activity::class.java))
}