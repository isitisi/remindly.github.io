package com.jitin.remindly.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jitin.remindly.data.EventEntity
import com.jitin.remindly.data.EventRepository
import com.jitin.remindly.data.TaskEntity
import com.jitin.remindly.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)
    private val eventRepository = EventRepository(application)

    val tasks: StateFlow<List<TaskEntity>> = taskRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val events: StateFlow<List<EventEntity>> = eventRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setTaskDone(task: TaskEntity, isDone: Boolean) {
        viewModelScope.launch { taskRepository.setDone(task, isDone) }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { taskRepository.delete(task) }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch { eventRepository.delete(event) }
    }
}
