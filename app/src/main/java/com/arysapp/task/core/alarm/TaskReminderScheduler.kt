package com.arysapp.task.core.alarm

import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.domain.usecase.TaskUseCases
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class TaskReminderScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val tasksUseCase: TaskUseCases
) {
    companion object {
        private const val TAG = "TaskReminderScheduler"
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    private val reminderOffsets = listOf(
        24 * 60 * 60 * 1000L, // 1 day before
        12 * 60 * 60 * 1000L, // 12 hours before
        60 * 60 * 1000L,      // 1 hour before
        30 * 60 * 1000L,      // 30 minutes before
        5 * 60 * 1000L,       // 5 minutes before
        60 * 1000L            // 1 minute before
    )

    suspend fun scheduleAllReminders() {
        try {
            val allTasks = tasksUseCase.getAllTasks().first()
            val tasksToSchedule = allTasks.filter { it.isActive && !it.dateTime.isNullOrBlank() && !it.hourTime.isNullOrBlank() }
            tasksToSchedule.forEach { task ->
                try {
                    scheduleReminderForTask(task)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to schedule for task id=${task.id}", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "scheduleAllReminders error", e)
        }
    }

    private fun scheduleReminderForTask(task: TaskModel) {
        val dueTime = parseDueTime(task) ?: run {
            Log.w(TAG, "parseDueTime returned null for task ${task.id}")
            return
        }

        val now = System.currentTimeMillis()

        reminderOffsets.forEachIndexed { index, offset ->
            val reminderTime = dueTime - offset
            if (reminderTime > now) {
                val pending = AlarmIntentFactory.createReminderPendingIntent(context, task, index)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pending)
                Log.d(TAG, "Scheduled reminder index=$index for task=${task.id} at=$reminderTime")
            }
        }

        if (dueTime > now) {
            val duePending = AlarmIntentFactory.createDuePendingIntent(context, task)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dueTime, duePending)
            Log.d(TAG, "Scheduled due alarm for task=${task.id} at=$dueTime")
        }
    }

    fun cancelReminderForTask(taskId: Long) {
        try {
            val dueIntent = Intent(context, TaskAlarmReceiver::class.java).apply { setPackage(context.packageName) }
            val duePending = android.app.PendingIntent.getBroadcast(
                context,
                taskId.toInt(),
                dueIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(duePending)
            duePending.cancel()

            reminderOffsets.forEachIndexed { index, _ ->
                val requestCode = taskId.toInt() * AlarmIntentFactory.REMINDER_REQUEST_CODE_MULTIPLIER + index
                val reminderIntent = Intent(context, TaskAlarmReceiver::class.java).apply { setPackage(context.packageName) }
                val reminderPending = android.app.PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    reminderIntent,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(reminderPending)
                reminderPending.cancel()
            }

            Log.d(TAG, "Cancelled reminders for taskId=$taskId")
        } catch (e: Exception) {
            Log.e(TAG, "cancelReminderForTask error for id=$taskId", e)
        }
    }

    private fun parseDueTime(task: TaskModel): Long? {
        return try {
//            val dateStr = if (USER_LANGUAGE == "fa") {
//                val parts = task.dateTime!!.trim().split("-")
//                val jDate = JalaliDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
//                val gDate = jDate.toGregorian()
//                "%04d-%02d-%02d".format(gDate[0], gDate[1], gDate[2])
//            } else {
//                task.dateTime!!.trim()
//            }
            val dateStr = task.dateTime!!.trim()
            val combined = "$dateStr ${task.hourTime!!.trim()}"
            formatter.parse(combined)?.time
        } catch (e: Exception) {
            Log.e(TAG, "parseDueTime parse error for task=${task.id}", e)
            null
        }
    }

    fun scheduleRemindersForTasks(tasks: List<TaskModel>) {
        tasks.forEach { task ->
            if (task.isActive && !task.dateTime.isNullOrBlank() && !task.hourTime.isNullOrBlank()) {
                scheduleReminderForTask(task)
            } else {
                cancelReminderForTask(task.id)
            }
        }
    }
}
