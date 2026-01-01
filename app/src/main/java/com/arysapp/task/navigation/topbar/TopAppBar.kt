package com.arysapp.task.navigation.topbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearching by remember { mutableStateOf(false) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    if (currentRoute == Screens.Home.route) {
        TopAppBar(
            modifier = modifier,
            title = {
                AnimatedContent(
                    targetState = isSearching,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "SearchBarTransition"
                ) { searching ->
                    if (searching) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            placeholder = { Text(text = stringResource(R.string.search)) },
                            singleLine = true,
                            leadingIcon = {
                                IconButton(onClick = { isSearching = false }) {
                                    Icon(
                                        painter = painterResource(R.drawable.arrow_back_icon),
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            },
            navigationIcon = {
                AnimatedVisibility(
                    visible = !isSearching,
                    //enter = fadeIn(),
                    //exit = fadeOut()
                ) {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            painter = painterResource(R.drawable.settings_filled),
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            actions = {
                AnimatedContent(
                    targetState = isSearching,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ActionsTransition"
                ) { searching ->
                    if (searching) {
                        IconButton(onClick = onFilterClick) {
                            Icon(
                                painter = painterResource(R.drawable.filter_icon_100px),
                                contentDescription = "Filter",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Row {
                            IconButton(onClick = { isSearching = true }) {
                                Icon(
                                    painter = painterResource(R.drawable.search_icon_1),
                                    contentDescription = "Search",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(onClick = onFilterClick) {
                                Icon(
                                    painter = painterResource(R.drawable.filter_icon_100px),
                                    contentDescription = "Filter",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
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
