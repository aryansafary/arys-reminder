package com.arysapp.reminder.navigation.bottombar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object BottomBarDefaults {
    val Height: Dp = 72.dp
    //val CornerRadius: Dp = 36.dp
    val HorizontalMargin: Dp = 8.dp
    val BottomMargin: Dp = 8.dp

    val FabSize: Dp = 64.dp
    val FabIconSize: Dp = 28.dp
    val FabOffset: Dp = (-18).dp
    val FabCutoutRadius: Dp = 40.dp

    val Elevation: Dp = 12.dp
    val FabElevation: Dp = 16.dp

    val ItemPaddingHorizontal: Dp = 20.dp
    val ItemPaddingVertical: Dp = 12.dp
    val ItemIconSize: Dp = 24.dp
    val ItemSpacing: Dp = 8.dp

    val OuterPadding = PaddingValues(
        start = HorizontalMargin,
        end = HorizontalMargin,
        bottom = BottomMargin
    )
}