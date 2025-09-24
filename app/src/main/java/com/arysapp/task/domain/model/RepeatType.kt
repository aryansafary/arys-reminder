package com.arysapp.task.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.arysapp.task.R

enum class RepeatType {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY;
    @Composable
    fun getLabel(): String {
        return when (this) {
            NONE -> stringResource(R.string.repeat_none)
            DAILY -> stringResource(R.string.repeat_daily)
            WEEKLY -> stringResource(R.string.repeat_weekly)
            MONTHLY -> stringResource(R.string.repeat_monthly)
        }
    }
}