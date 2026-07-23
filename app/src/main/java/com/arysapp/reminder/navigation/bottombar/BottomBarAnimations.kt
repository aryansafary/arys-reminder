package com.arysapp.reminder.navigation.bottombar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

object BottomBarAnimations {
    val colorTweenSpec = tween<Color>(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    )

    val alphaTweenSpec = tween<Float>(
        durationMillis = 150,
        easing = LinearEasing
    )

//    val indicatorTweenSpec = tween<Float>(
//        durationMillis = 200,
//        easing = FastOutSlowInEasing
//    )

    val scaleTweenSpec = tween<Float>(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    )

    val sizeTweenSpec = tween<IntSize>(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    )
}