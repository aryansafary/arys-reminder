package com.arysapp.task.domain.usecase

import javax.inject.Inject

data class TaskUseCases @Inject constructor(
    val insertTask: InsertTaskUseCase,
    val deleteTask: DeleteTaskUseCase,
    val updateTaskStatus: UpdateTaskStatusUseCase,
    val getAllTasks: GetAllTasksUseCase
)
