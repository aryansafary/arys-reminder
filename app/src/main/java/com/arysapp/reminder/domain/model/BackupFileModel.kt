package com.arysapp.reminder.domain.model

import java.io.File

data class BackupFileModel(
    val name: String,
    val file: File,
    val lastModified: Long
)
