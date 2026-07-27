package com.arysapp.reminder.navigation.topbar

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arysapp.reminder.R
import com.arysapp.reminder.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavController,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val titleText = when (currentRoute) {
        Screens.Settings.route -> stringResource(R.string.settings)
        Screens.AddReminder.withArgs("{task}") -> stringResource(R.string.add_reminder)
        else -> stringResource(R.string.app_name)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.855f),
        tonalElevation = 3.dp,
        shadowElevation = 6.dp
    ) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = {
                AnimatedContent(
                    targetState = titleText,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220)) +
                                slideInVertically(animationSpec = tween(220)) { height -> height / 2 })
                            .togetherWith(
                                fadeOut(animationSpec = tween(220)) +
                                        slideOutVertically(animationSpec = tween(220)) { height -> -height / 2 }
                            )
                    },
                    label = "TopBarTitleAnimation"
                ) { targetTitle ->
                    Text(
                        text = targetTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                if (currentRoute == Screens.Home.route || currentRoute == Screens.Calender.route) {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            painter = painterResource(R.drawable.settings_filled),
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    if (currentRoute?.contains(Screens.ShowAlarmScreen.route) == false) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_back_icon),
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            actions = {
                if (currentRoute != Screens.Home.route && currentRoute != Screens.Calender.route && currentRoute != Screens.Settings.route) {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            painter = painterResource(R.drawable.settings_filled),
                            contentDescription = "Settings",
                            modifier = Modifier.size(22.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        )
    }
}