package com.arysapp.reminder.utils.helper

import com.arysapp.reminder.domain.model.ReminderModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object JsonUtils {
    private val gson = Gson()

    fun List<ReminderModel>.toJsonFile(file: File): File {
        file.writeText(gson.toJson(this))
        return file
    }

    fun File.fromJsonToReminders(): List<ReminderModel> {
        val json = readText()
        val type = object : TypeToken<List<ReminderModel>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}