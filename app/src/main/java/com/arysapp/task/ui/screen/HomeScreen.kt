package com.arysapp.task.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.ui.components.TaskCard
import com.arysapp.task.ui.viewmodel.TaskViewModel
import com.arysapp.task.utils.StateResult
import com.arysapp.task.R
import com.arysapp.task.navigation.Screens
import com.google.gson.Gson


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    val tasksState by viewModel.tasks.collectAsState()
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (tasksState) {
                is StateResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is StateResult.Success -> {
                    val tasks = (tasksState as StateResult.Success<List<TaskModel>>).data
                    if (tasks.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.not_found_task),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(items = tasks, key = { it.id }) { task ->
                                TaskCard(
                                    task = task,
                                    onEdit = { task ->
                                        val gson = Gson()
                                        val taskJson = gson.toJson(task)
                                        navController.navigate(Screens.AddTask.withArgs(taskJson))

                                    },
                                    onDelete = { deletedTask ->
                                        viewModel.deleteTask(task)
                                    },
                                    onToggle = { newState ->
                                        viewModel.updateTaskStatus(task.id, newState)
                                    }
                                )
                            }
                        }
                    }
                }

                is StateResult.Error -> {
                    val message = (tasksState as StateResult.Error).message
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
                        Button(onClick = { viewModel.getAllTasks() }) {
                            Text(text = stringResource(R.string.try_again))
                        }
                    }
                }
            }
        }
    }
}


