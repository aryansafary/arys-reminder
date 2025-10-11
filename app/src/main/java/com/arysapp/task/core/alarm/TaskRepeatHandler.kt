package com.arysapp.task.core.alarm

import com.arysapp.task.domain.usecase.TaskUseCases
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class TaskRepeatHandler @Inject constructor(
    private val taskUseCases: TaskUseCases,
    private val reminderScheduler: TaskReminderScheduler
) {

    companion object {
        private const val TAG = "TaskRepeatHandler"
    }

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    suspend fun handleRepeat(taskId: Long, repeatType: String?, dateStr: String?, hourTime: String?) {
        try {
            when (repeatType?.uppercase(Locale.getDefault()) ?: "NONE") {
                "NONE" -> {
                    taskUseCases.updateTaskStatus(taskId, false)
                }
                "DAILY" -> {
                    val newDate = incrementDate(dateStr, 1)
                    taskUseCases.updateTaskDateTime(taskId, newDate, hourTime)
                }
                "WEEKLY" -> {
                    val newDate = incrementDate(dateStr, 7)
                    taskUseCases.updateTaskDateTime(taskId, newDate, hourTime)
                }
                "MONTHLY" -> {
                    val newDate = incrementMonth(dateStr)
                    taskUseCases.updateTaskDateTime(taskId, newDate, hourTime)
                }
                else -> {
                    taskUseCases.updateTaskStatus(taskId, false)
                }
            }


            reminderScheduler.cancelReminderForTask(taskId)
            reminderScheduler.scheduleAllReminders()

            Log.d(TAG, "handleRepeat completed for taskId=$taskId repeatType=$repeatType")
        } catch (e: Exception) {
            Log.e(TAG, "handleRepeat error for taskId=$taskId", e)
        }
    }

    private fun incrementDate(dateStr: String?, days: Int): String {
        if (dateStr.isNullOrBlank()) return dateStr ?: ""
        return try {
            val cal = Calendar.getInstance().apply {
                time = dateFormatter.parse(dateStr) ?: return dateStr
                add(Calendar.DAY_OF_MONTH, days)
            }
            dateFormatter.format(cal.time)
        } catch (e: Exception) {
            e.printStackTrace()
            dateStr
        }
    }

    private fun incrementMonth(dateStr: String?): String {
        if (dateStr.isNullOrBlank()) return dateStr ?: ""
        return try {
            val cal = Calendar.getInstance().apply {
                time = dateFormatter.parse(dateStr) ?: return dateStr
                add(Calendar.MONTH, 1)
            }
            dateFormatter.format(cal.time)
        } catch (e: Exception) {
            e.printStackTrace()
            dateStr
        }
    }
}
