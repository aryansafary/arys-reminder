package com.arysapp.task.data.local.dao
import androidx.room.*
import com.arysapp.task.data.local.entity.TaskEntity
import com.arysapp.task.utils.Constants.COLUMN_TASK_CREATED_AT
import com.arysapp.task.utils.Constants.COLUMN_TASK_ID
import com.arysapp.task.utils.Constants.COLUMN_TASK_IS_ACTIVE
import com.arysapp.task.utils.Constants.COLUMN_TASK_START_TIME
import com.arysapp.task.utils.Constants.TASK_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>): List<Long>

    @Update
    suspend fun updateTask(task: TaskEntity): Int


    @Delete
    suspend fun deleteTask(task: TaskEntity): Int

    @Query("DELETE FROM $TASK_TABLE_NAME WHERE $COLUMN_TASK_ID = :id")
    suspend fun deleteTaskById(id: Long): Int

    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE $COLUMN_TASK_ID = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Query("SELECT * FROM $TASK_TABLE_NAME ORDER BY $COLUMN_TASK_CREATED_AT DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE $COLUMN_TASK_IS_ACTIVE = 1 ORDER BY $COLUMN_TASK_START_TIME ASC")
    fun getActiveTasks(): Flow<List<TaskEntity>>

    @Query("UPDATE $TASK_TABLE_NAME SET $COLUMN_TASK_IS_ACTIVE = :isActive WHERE $COLUMN_TASK_ID = :id")
    suspend fun updateTaskStatus(id: Long, isActive: Boolean): Int
}
