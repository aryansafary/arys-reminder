package com.arysapp.reminder.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arysapp.reminder.R
import com.arysapp.reminder.navigation.Screens

@Composable
fun MyFloatingActionButton(navController: NavController,modifier: Modifier = Modifier) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    if (backStackEntry?.destination?.route===Screens.Home.route)
     {
        FloatingActionButton(
            modifier =modifier,
            onClick = { navController.navigate(Screens.AddReminder.route) },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_reminder_filled),
                contentDescription = null,
                modifier  = Modifier.size(24.dp)
            )
        }
    }
}

