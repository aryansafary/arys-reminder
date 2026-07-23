package com.arysapp.reminder.ui.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import com.arysapp.reminder.navigation.bottombar.BottomBarColors
import com.arysapp.reminder.navigation.bottombar.BottomBarDefaults

@Composable
fun FloatingFab(
    icon: Painter,
    colors: BottomBarColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier.size(BottomBarDefaults.FabSize),
        shape = CircleShape,
        color = colors.fabContainerColor,
        contentColor = colors.fabContentColor,
        shadowElevation = BottomBarDefaults.FabElevation
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = colors.fabContentColor),
                    role = Role.Button,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "Add",
                modifier = Modifier.size(BottomBarDefaults.FabIconSize),
                tint = colors.fabContentColor
            )
        }
    }
}