package com.arysapp.reminder.navigation.bottombar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BottomBarItem(
    item: BottomNavItem,
    isSelected: Boolean,
    colors: BottomBarColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) colors.selectedIconColor else colors.unselectedIconColor,
        animationSpec = BottomBarAnimations.colorTweenSpec,
        label = "BottomBarItemIconColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) colors.selectedTextColor else colors.unselectedTextColor,
        animationSpec = BottomBarAnimations.colorTweenSpec,
        label = "BottomBarItemTextColor"
    )

//    val indicatorAlpha by animateFloatAsState(
//        targetValue = if (isSelected) 1f else 0f,
//        animationSpec = BottomBarAnimations.indicatorTweenSpec,
//        label = "BottomBarItemIndicatorAlpha"
//    )

    val itemScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = BottomBarAnimations.scaleTweenSpec,
        label = "BottomBarItemScale"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .scale(itemScale)
            .clip(CircleShape)
            .background(
                color = Color.Transparent,
                  //color = colors.indicatorColor
                  //  .copy(alpha = indicatorAlpha),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = colors.unselectedIconColor),
                role = Role.Tab,
                onClick = onClick
            )
            .animateContentSize(animationSpec = BottomBarAnimations.sizeTweenSpec)
            .padding(
                horizontal = BottomBarDefaults.ItemPaddingHorizontal,
                vertical = BottomBarDefaults.ItemPaddingVertical
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = item.icon,
                contentDescription = item.name,
                tint = iconColor,
                modifier = Modifier.size(BottomBarDefaults.ItemIconSize).align(
                    Alignment.CenterVertically
                )
            )
                AnimatedVisibility(
                    visible = isSelected,
                    enter = fadeIn(animationSpec = BottomBarAnimations.alphaTweenSpec),
                    exit = fadeOut(animationSpec = BottomBarAnimations.alphaTweenSpec)
                )
                {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(BottomBarDefaults.ItemSpacing))
                        Text(
                            text = item.name,
                            color = textColor,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }


                }

            }

        }
