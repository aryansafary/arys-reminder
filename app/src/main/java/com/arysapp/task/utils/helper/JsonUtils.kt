package com.arysapp.task.utils.helper

import com.arysapp.task.domain.model.TaskModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object JsonUtils {
    private val gson = Gson()

    fun List<TaskModel>.toJsonFile(file: File): File {
        file.writeText(gson.toJson(this))
        return file
    }

    fun File.fromJsonToTasks(): List<TaskModel> {
        val json = readText()
        val type = object : TypeToken<List<TaskModel>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}