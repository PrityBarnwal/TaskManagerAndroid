package com.example.kalagatotask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kalagatotask.repo.TaskRepository
import com.example.kalagatotask.room.AppDatabase
import com.example.kalagatotask.room.TaskEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val tasks: Flow<List<TaskEntity>>

    private val _isLoading = MutableStateFlow(true)  // Loading state
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        tasks = repository.getAllTasks()
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1500)
            _isLoading.value = false
        }
    }


    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
    fun getTaskById(taskId: Long): Flow<TaskEntity> = repository.getTaskById(taskId)


    fun deleteAllTask(){
        viewModelScope.launch {
            repository.deleteAllTask()
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
}
