package com.arysapp.reminder.core.alarm

import com.arysapp.reminder.domain.usecase.ReminderUseCases
import android.util.Log
import com.arysapp.reminder.domain.model.ReminderModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class ReminderRepeatHandler @Inject constructor(
    private val reminderUseCases: ReminderUseCases,
    private val reminderScheduler: ReminderScheduler
) {

    companion object {
        private const val TAG = "ReminderRepeatHandler"
    }

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    suspend fun handleRepeat(reminderId: Long, repeatType: String?, dateStr: String?, hourTime: String?) {
        try {
            when (repeatType?.uppercase(Locale.getDefault()) ?: "NONE") {
                "NONE" -> {
                    val reminderToDelete = ReminderModel(
                        id = reminderId,
                        title = "",
                        description = "",
                        dateTime = dateStr,
                        hourTime = hourTime,
                        repeatType = "NONE",
                        isActive = true,
                        createdAt = "",
                        updatedAt = "",
                    )
                    reminderUseCases.updateReminderStatus(id = reminderId, isActive = false)
                    reminderUseCases.processExpiredReminders()
                    reminderUseCases.deleteReminder(reminderToDelete)
                }
                "DAILY" -> {
                    val newDate = incrementDate(dateStr, 1)
                    reminderUseCases.updateReminderDateTime(reminderId, newDate, hourTime)
                }
                "WEEKLY" -> {
                    val newDate = incrementDate(dateStr, 7)
                    reminderUseCases.updateReminderDateTime(reminderId, newDate, hourTime)
                }
                "MONTHLY" -> {
                    val newDate = incrementMonth(dateStr)
                    reminderUseCases.updateReminderDateTime(reminderId, newDate, hourTime)
                }
                else -> {
                    reminderUseCases.updateReminderStatus(reminderId, false)
                }
            }


            reminderScheduler.cancelReminderForReminder(reminderId)
            reminderScheduler.scheduleAllReminders()

            Log.d(TAG, "handleRepeat completed for reminderId=$reminderId repeatType=$repeatType")
        } catch (e: Exception) {
            Log.e(TAG, "handleRepeat error for reminderId=$reminderId", e)
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
