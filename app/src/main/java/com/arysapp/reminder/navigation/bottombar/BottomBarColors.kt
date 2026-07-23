package com.arysapp.reminder.navigation.bottombar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class BottomBarColors(
    val containerColor: Color,
    val selectedIconColor: Color,
    val unselectedIconColor: Color,
    val selectedTextColor: Color,
    val unselectedTextColor: Color,
    val indicatorColor: Color,
    val fabContainerColor: Color,
    val fabContentColor: Color
)

object BottomBarColorDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
        selectedIconColor: Color = MaterialTheme.colorScheme.primary,
        unselectedIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedTextColor: Color = MaterialTheme.colorScheme.primary,
        unselectedTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        indicatorColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        fabContainerColor: Color = MaterialTheme.colorScheme.primary,
        fabContentColor: Color = MaterialTheme.colorScheme.onPrimary
    ): BottomBarColors = BottomBarColors(
        containerColor = containerColor,
        selectedIconColor = selectedIconColor,
        unselectedIconColor = unselectedIconColor,
        selectedTextColor = selectedTextColor,
        unselectedTextColor = unselectedTextColor,
        indicatorColor = indicatorColor,
        fabContainerColor = fabContainerColor,
        fabContentColor = fabContentColor
    )
}