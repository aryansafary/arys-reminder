package com.arysapp.reminder.core.alarm

import android.util.Log
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.usecase.ReminderUseCases
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

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    suspend fun handleRepeat(reminderId: Long, repeatType: String?, dateStr: String?, hourTime: String?) {
        try {
            val normalizedRepeatType = repeatType?.uppercase(Locale.US) ?: RepeatType.NONE.name

            when (normalizedRepeatType) {
                RepeatType.NONE.name -> {
                    reminderUseCases.deleteReminder(
                        ReminderModel(
                            id = reminderId,
                            title = "",
                            description = "",
                            dateTime = dateStr,
                            hourTime = hourTime,
                            repeatType = RepeatType.NONE.name,
                            isActive = false,
                            createdAt = "",
                            updatedAt = ""
                        )
                    )
                }
                RepeatType.DAILY.name -> {
                    val newDate = incrementDate(dateStr, 1)
                    reminderUseCases.updateReminderDateTime(reminderId, newDate, hourTime)
                }
                RepeatType.WEEKLY.name -> {
                    val newDate = incrementDate(dateStr, 7)
                    reminderUseCases.updateReminderDateTime(reminderId, newDate, hourTime)
                }
                RepeatType.MONTHLY.name -> {
                    val newDate = incrementMonth(dateStr)
                    reminderUseCases.updateReminderDateTime(reminderId, newDate, hourTime)
                }
                RepeatType.YEARLY.name -> {
                    val newDate = incrementYear(dateStr)
                    reminderUseCases.updateReminderDateTime(reminderId, newDate, hourTime)
                }
                else -> {
                    reminderUseCases.updateReminderStatus(reminderId, false)
                }
            }

            reminderScheduler.cancelReminderForReminder(reminderId)
            reminderScheduler.scheduleAllReminders()

            Log.d(TAG, "handleRepeat completed for reminderId=$reminderId repeatType=$normalizedRepeatType")
        } catch (e: Exception) {
            Log.e(TAG, "handleRepeat error for reminderId=$reminderId", e)
        }
    }

    private fun incrementDate(dateStr: String?, days: Int): String {
        if (dateStr.isNullOrBlank()) return dateStr ?: ""
        return try {
            val cal = Calendar.getInstance(Locale.US).apply {
                time = dateFormatter.parse(dateStr) ?: return dateStr
                add(Calendar.DAY_OF_MONTH, days)
            }
            dateFormatter.format(cal.time)
        } catch (e: Exception) {
            Log.e(TAG, "Error incrementing date: $dateStr", e)
            dateStr
        }
    }

    private fun incrementMonth(dateStr: String?): String {
        if (dateStr.isNullOrBlank()) return dateStr ?: ""
        return try {
            val cal = Calendar.getInstance(Locale.US).apply {
                time = dateFormatter.parse(dateStr) ?: return dateStr
                add(Calendar.MONTH, 1)
            }
            dateFormatter.format(cal.time)
        } catch (e: Exception) {
            Log.e(TAG, "Error incrementing month: $dateStr", e)
            dateStr
        }
    }

    private fun incrementYear(dateStr: String?): String {
        if (dateStr.isNullOrBlank()) return dateStr ?: ""
        return try {
            val cal = Calendar.getInstance(Locale.US).apply {
                time = dateFormatter.parse(dateStr) ?: return dateStr
                add(Calendar.YEAR, 1)
            }
            dateFormatter.format(cal.time)
        } catch (e: Exception) {
            Log.e(TAG, "Error incrementing year: $dateStr", e)
            dateStr
        }
    }
}