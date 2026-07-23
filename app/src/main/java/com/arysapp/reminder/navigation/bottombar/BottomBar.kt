package com.arysapp.reminder.navigation.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arysapp.reminder.R
import com.arysapp.reminder.navigation.Screens
import com.arysapp.reminder.ui.components.FloatingFab
@Composable
fun MyBottomBar(
    navController: NavHostController,
    onItemClicked: (BottomNavItem) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: BottomBarColors = BottomBarColorDefaults.colors(),
    fabIcon: Painter = painterResource(R.drawable.add_reminder_filled)
) {
    val bottomBarShape = remember { BottomBarShape() }
    val items = listOf(
        BottomNavItem(
            name = stringResource(R.string.Home),
            route = Screens.Home.route,
            icon = painterResource(R.drawable.home_filled)
        ),
        BottomNavItem(
            name = stringResource(R.string.calendar),
            route = Screens.Calender.route,
            icon = painterResource(R.drawable.calendar_icon)
        )
    )
    val backStackEntry = navController.currentBackStackEntryAsState()
    val showBottomBar = backStackEntry.value?.destination?.route in items.map { it.route }
    if (showBottomBar) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(BottomBarDefaults.OuterPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BottomBarDefaults.Height),
                shape = bottomBarShape,
                color = colors.containerColor,
                shadowElevation = BottomBarDefaults.Elevation
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BottomBarDefaults.HorizontalMargin),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                        BottomBarItem(
                            item = item,
                            isSelected = item.route == backStackEntry.value?.destination?.route,
                            colors = colors,
                            onClick = { onItemClicked(item) }
                        )
                        }
                   if(index==0)
                    Spacer(modifier = Modifier.width(BottomBarDefaults.FabSize))

            }

              }
            }

            FloatingFab(
                icon = fabIcon,
                colors = colors,
                onClick = onFabClick,
                modifier = Modifier
                    .zIndex(2f)
                    .offset(y = BottomBarDefaults.FabOffset)
            )
        }
    }
}