package com.arysapp.task.navigation.topbar
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arysapp.task.R
import com.arysapp.task.navigation.Screens
@ExperimentalMaterial3Api
@Composable
fun MyTopAppBar(
    navController: NavController,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    if (currentRoute == Screens.Home.route) {
        TopAppBar(
            modifier = modifier,
            title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
            navigationIcon = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            painter = painterResource(R.drawable.settings_filled),
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )
                    }
            },
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = when (currentRoute) {
                        Screens.Settings.route -> stringResource(R.string.settings)
                        Screens.AddTask.withArgs("{task}") -> stringResource(R.string.add_task)
                        else -> stringResource(R.string.app_name)
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            },
    navigationIcon = {
        if(currentRoute?.contains(Screens.ShowAlarmScreen.route) == false)
        IconButton(onClick = { navController.popBackStack() }) {

            Icon(
                painter = painterResource(R.drawable.arrow_back_icon),
                contentDescription = "Back"
            )
        }


}


        )
    }
}
