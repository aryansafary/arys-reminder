package com.arysapp.reminder.domain.usecase

import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.repository.ReminderRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ProcessExpiredRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    suspend operator fun invoke() {
        // دریافت یک‌باره تمام تسک‌ها از دیتابیس
        val tasks = reminderRepository.getAllTasksOnce()

        // زمان فعلی سیستم بدون ثانیه و میلی‌ثانیه برای مقایسه دقیق
        val nowCal = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        tasks.forEach { task ->
            val dateStr = task.dateTime
            val timeStr = task.hourTime

            // بررسی خالی نبودن تاریخ و ساعت تسک
            if (!dateStr.isNullOrBlank() && !timeStr.isNullOrBlank()) {
                val taskCal = parseToCalendar(dateStr, timeStr)

                // اگر زمان تسک فرارسیده یا گذشته باشد (منقضی شده)
                if (taskCal != null && taskCal.before(nowCal) ||taskCal !=null && taskCal == nowCal) {

                    // تشخیص نوع تکرار تسک
                    val repeatType = runCatching { RepeatType.valueOf(task.repeatType) }
                        .getOrElse { RepeatType.NONE }

                    if (repeatType == RepeatType.NONE) {
                        // 🟢 ایده شما: تسک بدون تکرار منقضی شده، پس کلاً از دیتابیس حذف می‌شود
                        reminderRepository.deleteTask(task)
                    } else {
                        // تسک تکرار شونده است؛ محاسبه زمان آلارم بعدی در آینده
                        val nextCal = taskCal.clone() as Calendar

                        while (!nextCal.after(nowCal)) {
                            when (repeatType) {
                                RepeatType.DAILY -> nextCal.add(Calendar.DAY_OF_YEAR, 1)
                                RepeatType.WEEKLY -> nextCal.add(Calendar.WEEK_OF_YEAR, 1)
                                RepeatType.MONTHLY -> nextCal.add(Calendar.MONTH, 1)
                                else -> nextCal.add(Calendar.DAY_OF_YEAR, 1)
                            }
                        }

                        // به‌روزرسانی تسک با تاریخ جدید
                        val updatedTask = task.copy(
                            dateTime = dateFormat.format(nextCal.time),
                            updatedAt = nowAsString()
                        )
                        reminderRepository.updateTask(updatedTask)
                    }
                }
            }
        }
    }

    /**
     * تبدیل رشته تاریخ و ساعت به یک شیء Calendar واحد و دقیق
     */
    private fun parseToCalendar(dateStr: String, timeStr: String): Calendar? {
        return try {
            val parsedDate: Date = dateFormat.parse(dateStr) ?: return null
            val parsedTime: Date = timeFormat.parse(timeStr) ?: return null

            val dateCal = Calendar.getInstance().apply { time = parsedDate }
            val timeCal = Calendar.getInstance().apply { time = parsedTime }

            Calendar.getInstance().apply {
                set(Calendar.YEAR, dateCal.get(Calendar.YEAR))
                set(Calendar.MONTH, dateCal.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, dateCal.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun nowAsString(): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.format(Date())
    }
}