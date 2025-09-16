package com.arysapp.task.navigation


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arysapp.task.R
import com.arysapp.task.ui.theme.BottomBarColors


@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    onItemClick: (BottomBarItem) -> Unit
){
   val items = listOf(
       BottomBarItem(
           name = stringResource(R.string.Home),
           selectedIcon = painterResource(R.drawable.home_filled),
           unselectedIcon = painterResource(R.drawable.home_outlined),
           route = Screens.Home.route
       ),
       BottomBarItem(
           name = stringResource(R.string.Calendar),
           selectedIcon = painterResource(R.drawable.calendar_filled),
           unselectedIcon = painterResource(R.drawable.calendar_outlined),
           route = Screens.Calendar.route
       ),
       BottomBarItem(
           name = stringResource(R.string.Settings),
           selectedIcon = painterResource(R.drawable.settings_filled),
           unselectedIcon = painterResource(R.drawable.settings_outlined),
           route = Screens.Settings.route
       )
   )
val backStackEntry = navController.currentBackStackEntryAsState()
val showBottomBar = backStackEntry.value?.destination?.route in items.map { it.route }
    if (showBottomBar) {
        BottomAppBar(
            containerColor = if (!isSystemInDarkTheme()) {
                BottomBarColors.LightContainer
            } else {
                BottomBarColors.DarkContainer
            }
        ) {
            items.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BottomBarColors.Selected,
                        selectedTextColor = BottomBarColors.Selected,
                        unselectedIconColor = BottomBarColors.Unselected,
                        unselectedTextColor = BottomBarColors.Unselected,
                        indicatorColor = if (!isSystemInDarkTheme()) {
                            BottomBarColors.LightIndicator
                        } else {
                            BottomBarColors.DarkIndicator
                        }
                    ),
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.name,
                                modifier = Modifier.height(24.dp),
                                tint = if (selected) BottomBarColors.Selected else BottomBarColors.Unselected,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,

                                )
                        }
                    },

                    )

            }
        }


    }
}