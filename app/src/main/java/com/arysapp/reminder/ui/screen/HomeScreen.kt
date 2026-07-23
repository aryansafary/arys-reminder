package com.arysapp.reminder.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.ui.components.ReminderCard
import com.arysapp.reminder.ui.viewmodel.ReminderViewModel
import com.arysapp.reminder.utils.StateResult
import com.arysapp.reminder.R
import com.arysapp.reminder.navigation.Screens
import com.google.gson.Gson


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel(),
) {
    val remindersState by viewModel.reminders.collectAsState()
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (remindersState) {
                is StateResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is StateResult.Success -> {
                    val reminders = (remindersState as StateResult.Success<List<ReminderModel>>).data
                    if (reminders.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
EmptyStateLottie()
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(items = reminders, key = { it.id }) { reminder ->
                                ReminderCard(
                                    reminder = reminder,
                                    onEdit = { reminder ->
                                        val gson = Gson()
                                        val reminderJson = gson.toJson(reminder)
                                        navController.navigate(Screens.AddReminder.withArgs(reminderJson))

                                    },
                                    onDelete = { reminder ->
                                        viewModel.deleteReminder(reminder)
                                    },
                                    onToggle = { newState ->
                                        viewModel.updateReminderStatus(reminder.id, newState)
                                    }
                                )
                            }
                        }
                    }
                }

                is StateResult.Error -> {
                    val message = (remindersState as StateResult.Error).message
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.getAllReminders() }) {
                            Text(text = stringResource(R.string.try_again))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateLottie() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_anim))
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(250.dp)
        )
        Text(text = stringResource(R.string.not_found_reminder),
            style = MaterialTheme.typography.bodyLarge)
    }
}


