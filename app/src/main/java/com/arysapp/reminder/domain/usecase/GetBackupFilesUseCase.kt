package com.arysapp.reminder.domain.usecase

import android.os.Environment
import com.arysapp.reminder.domain.model.BackupFileModel
import jakarta.inject.Inject
import java.io.File

class GetBackupFilesUseCase @Inject constructor() {

    operator fun invoke(): List<BackupFileModel> {

        @Suppress("DEPRECATION")
        val backupFolder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Arys"
        )

        if (!backupFolder.exists()) {
            return emptyList()
        }

        return backupFolder
            .listFiles()
            ?.filter {
                it.isFile &&
                        it.extension.equals("json", true)
            }
            ?.sortedByDescending {
                it.lastModified()
            }
            ?.map {
                BackupFileModel(
                    name = it.name,
                    file = it,
                    lastModified = it.lastModified()
                )
            }
            ?: emptyList()
    }
}