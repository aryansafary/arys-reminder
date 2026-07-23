package com.arysapp.reminder.navigation.bottombar

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

class BottomBarShape(
    private val cutoutRadius: Dp = BottomBarDefaults.FabCutoutRadius,
    private val fabOffset: Dp = BottomBarDefaults.FabOffset,
    private val fabSize: Dp = BottomBarDefaults.FabSize
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        val barPath = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
                )
            )
        }

        val cutoutPath = Path().apply {
            val radiusPx = with(density) { cutoutRadius.toPx() }
            val centerXPx = size.width / 2f

            // Calculate FAB center Y relative to the top of the BottomBar (0f)
            val fabTopYPx = with(density) { fabOffset.toPx() }
            val fabCenterYPx = fabTopYPx + with(density) { (fabSize / 2).toPx() }

            addOval(
                Rect(
                    left = centerXPx - radiusPx,
                    top = fabCenterYPx - radiusPx,
                    right = centerXPx + radiusPx,
                    bottom = fabCenterYPx + radiusPx
                )
            )
        }

        val resultPath = Path().apply {
            // Subtract the cutout from the capsule shape to create a perfect negative space
            op(barPath, cutoutPath, PathOperation.Difference)
        }

        return Outline.Generic(resultPath)
    }
}