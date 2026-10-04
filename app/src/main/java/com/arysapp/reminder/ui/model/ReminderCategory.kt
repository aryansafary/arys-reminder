package com.arysapp.reminder.ui.model
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.arysapp.reminder.R

sealed class ReminderCategory(
    val key: String,
    @param:StringRes val titleResId: Int,
    @param:DrawableRes val iconResId: Int,
    val lightColor: Color,
    val darkColor: Color
) {
    object Personal : ReminderCategory(
        key = "PERSONAL",
        titleResId = R.string.category_personal,
        iconResId = R.drawable.personal_time,
        lightColor = Color(0xFF2196F3),
        darkColor = Color(0xFF64B5F6)
    )

    object Work : ReminderCategory(
        key = "WORK",
        titleResId = R.string.category_work,
        iconResId = R.drawable.work_times,
        lightColor = Color(0xFFFF9800),
        darkColor = Color(0xFFFFB74D)
    )

    object GymAndHealth : ReminderCategory(
        key = "GYM_HEALTH",
        titleResId = R.string.category_gym_health,
        iconResId = R.drawable.gym_time,
        lightColor = Color(0xFF4CAF50),
        darkColor = Color(0xFF81C784)
    )


    object Study : ReminderCategory(
        key = "STUDY",
        titleResId = R.string.category_study,
        iconResId = R.drawable.reading_time,
        lightColor = Color(0xFF9C27B0),
        darkColor = Color(0xFFBA68C8)
    )


    object Shopping : ReminderCategory(
        key = "SHOPPING",
        titleResId = R.string.category_shopping,
        iconResId = R.drawable.shop_time,
        lightColor = Color(0xFFE91E63),
        darkColor = Color(0xFFF06292)
    )


    object Meeting : ReminderCategory(
        key = "MEETING",
        titleResId = R.string.category_meeting,
        iconResId = R.drawable.meet_time,
        lightColor = Color(0xFF00BCD4),
        darkColor = Color(0xFF4DD0E1)
    )

    object General : ReminderCategory(
        key = "GENERAL",
        titleResId = R.string.category_general,
        iconResId = R.drawable.times,
        lightColor = Color(0xFF607D8B),
        darkColor = Color(0xFF90A4AE)
    )

    companion object {

        fun fromKey(key: String?): ReminderCategory {
            return when (key?.uppercase()) {
                "PERSONAL" -> Personal
                "WORK" -> Work
                "GYM_HEALTH" -> GymAndHealth
                "STUDY" -> Study
                "SHOPPING" -> Shopping
                "MEETING" -> Meeting
                else -> General
            }
        }

        fun getAllCategories(): List<ReminderCategory> {
            return listOf(General, Personal, Work, GymAndHealth, Study, Shopping, Meeting)
        }
    }
}