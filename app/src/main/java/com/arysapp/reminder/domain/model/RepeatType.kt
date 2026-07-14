package com.arysapp.reminder.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.arysapp.reminder.R

enum class RepeatType {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;
    @Composable
    fun getLabel(): String {
        return when (this) {
            NONE -> stringResource(R.string.repeat_none)
            DAILY -> stringResource(R.string.repeat_daily)
            WEEKLY -> stringResource(R.string.repeat_weekly)
            MONTHLY -> stringResource(R.string.repeat_monthly)
            YEARLY -> stringResource(R.string.yearly_repeat)
        }
    }
}