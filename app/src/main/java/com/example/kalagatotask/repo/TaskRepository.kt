package com.example.kalagatotask.repo

import com.example.kalagatotask.room.TaskDao
import com.example.kalagatotask.room.TaskEntity
import kotlinx.coroutines.flow.Flow


class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    fun getTaskById(taskId: Long): Flow<TaskEntity> {
        return taskDao.getTaskById(taskId)
    }

    suspend fun deleteAllTask() {
        taskDao.deleteAllTasks()
    }

}
