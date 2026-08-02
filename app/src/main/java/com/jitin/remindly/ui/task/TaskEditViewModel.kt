package com.jitin.remindly.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jitin.remindly.data.Priority
import com.jitin.remindly.data.TaskEntity
import com.jitin.remindly.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class TaskEditState(
    val id: Long = 0,
    val title: String = "",
    val notes: String = "",
    val dueDateTime: LocalDateTime? = null,
    val priority: Priority = Priority.MEDIUM,
    val isDone: Boolean = false,
    val reminderDateTime: LocalDateTime? = null,
    val isLoading: Boolean = true,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)

class TaskEditViewModel(application: Application, private val taskId: Long) : AndroidViewModel(application) {
    private val repository = TaskRepository(application)

    private val _state = MutableStateFlow(TaskEditState(isNew = taskId == 0L))
    val state: StateFlow<TaskEditState> = _state.asStateFlow()

    init {
        if (taskId != 0L) {
            viewModelScope.launch {
                repository.getById(taskId)?.let { task ->
                    _state.value = TaskEditState(
                        id = task.id,
                        title = task.title,
                        notes = task.notes,
                        dueDateTime = task.dueDateTime,
                        priority = task.priority,
                        isDone = task.isDone,
                        reminderDateTime = task.reminderDateTime,
                        isLoading = false,
                        isNew = false
                    )
                }
            }
        } else {
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun updateTitle(title: String) { _state.value = _state.value.copy(title = title) }
    fun updateNotes(notes: String) { _state.value = _state.value.copy(notes = notes) }
    fun updateDueDateTime(value: LocalDateTime?) { _state.value = _state.value.copy(dueDateTime = value) }
    fun updatePriority(priority: Priority) { _state.value = _state.value.copy(priority = priority) }
    fun updateReminder(value: LocalDateTime?) { _state.value = _state.value.copy(reminderDateTime = value) }

    fun save() {
        val s = _state.value
        if (s.title.isBlank()) return
        viewModelScope.launch {
            repository.save(
                TaskEntity(
                    id = s.id,
                    title = s.title.trim(),
                    notes = s.notes.trim(),
                    dueDateTime = s.dueDateTime,
                    priority = s.priority,
                    isDone = s.isDone,
                    reminderDateTime = s.reminderDateTime
                )
            )
            _state.value = _state.value.copy(saved = true)
        }
    }

    fun delete() {
        val s = _state.value
        if (s.isNew) return
        viewModelScope.launch {
            repository.delete(
                TaskEntity(
                    id = s.id,
                    title = s.title,
                    notes = s.notes,
                    dueDateTime = s.dueDateTime,
                    priority = s.priority,
                    isDone = s.isDone,
                    reminderDateTime = s.reminderDateTime
                )
            )
            _state.value = _state.value.copy(deleted = true)
        }
    }
}

class TaskEditViewModelFactory(
    private val application: Application,
    private val taskId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TaskEditViewModel(application, taskId) as T
}
