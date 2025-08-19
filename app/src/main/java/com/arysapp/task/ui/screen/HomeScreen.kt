package com.arysapp.task.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arysapp.task.domain.model.TaskModel
import com.arysapp.task.ui.viewmodel.TaskViewModel
import com.arysapp.task.utils.StateResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasksState by viewModel.tasks.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var taskToEdit by remember { mutableStateOf<TaskModel?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "لیست تسک‌ها") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                taskToEdit = null // برای افزودن تسک جدید، حالت ویرایش را خالی می‌کنیم
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "افزودن تسک")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = tasksState) {
                is StateResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is StateResult.Success -> {
                    TaskList(
                        tasks = result.data,
                        onTaskClick = { task ->
                            taskToEdit = task
                            showDialog = true
                        },
                        onTaskDelete = { task -> viewModel.deleteTask(task) },
                        onTaskStatusChange = { taskId, isActive ->
                            viewModel.updateTaskStatus(taskId, isActive)
                        }
                    )
                }
                is StateResult.Error -> {
                    Text(text = "خطا: ${result.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    if (showDialog) {
        TaskEditDialog(
            task = taskToEdit,
            onDismiss = { showDialog = false },
            onSave = { updatedTask ->
                scope.launch {
                    if (updatedTask.id > 0) {
                        viewModel.updateTask(updatedTask)
                    } else {
                        viewModel.insertTask(
                            title = updatedTask.title,
                            description = updatedTask.description
                        )
                    }
                    showDialog = false
                    taskToEdit = null
                }
            }
        )
    }
}

@Composable
fun TaskList(
    tasks: List<TaskModel>,
    onTaskClick: (TaskModel) -> Unit,
    onTaskDelete: (TaskModel) -> Unit,
    onTaskStatusChange: (Long, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onClick = onTaskClick,
                onDelete = onTaskDelete,
                onStatusChange = onTaskStatusChange
            )
        }
    }
}

@Composable
fun TaskItem(
    task: TaskModel,
    onClick: (TaskModel) -> Unit,
    onDelete: (TaskModel) -> Unit,
    onStatusChange: (Long, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(task) }) ,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                )
                task.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = task.isActive,
                onCheckedChange = { onStatusChange(task.id, it) }
            )
            IconButton(onClick = { onDelete(task) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف تسک",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun TaskEditDialog(
    task: TaskModel?,
    onDismiss: () -> Unit,
    onSave: (TaskModel) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (task != null) "ویرایش تسک" else "افزودن تسک")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("توضیحات (اختیاری)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        task?.copy(title = title, description = description)
                            ?: TaskModel(
                                id = 0,
                                title = title,
                                description = description,
                                isActive = true,
                                createdAt = "2027",
                                updatedAt = "2025",
                            )
                    )
                },
                enabled = title.isNotBlank()
            ) {
                Text(text = "ذخیره")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "لغو")
            }
        }
    )
}




