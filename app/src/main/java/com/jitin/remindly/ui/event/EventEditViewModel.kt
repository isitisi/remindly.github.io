package com.jitin.remindly.ui.event

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jitin.remindly.data.EventEntity
import com.jitin.remindly.data.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class EventEditState(
    val id: Long = 0,
    val title: String = "",
    val notes: String = "",
    val location: String = "",
    val startDateTime: LocalDateTime = LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0),
    val endDateTime: LocalDateTime? = null,
    val reminderDateTime: LocalDateTime? = null,
    val isLoading: Boolean = true,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)

class EventEditViewModel(application: Application, private val eventId: Long) : AndroidViewModel(application) {
    private val repository = EventRepository(application)

    private val _state = MutableStateFlow(EventEditState(isNew = eventId == 0L))
    val state: StateFlow<EventEditState> = _state.asStateFlow()

    init {
        if (eventId != 0L) {
            viewModelScope.launch {
                repository.getById(eventId)?.let { event ->
                    _state.value = EventEditState(
                        id = event.id,
                        title = event.title,
                        notes = event.notes,
                        location = event.location,
                        startDateTime = event.startDateTime,
                        endDateTime = event.endDateTime,
                        reminderDateTime = event.reminderDateTime,
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
    fun updateLocation(location: String) { _state.value = _state.value.copy(location = location) }
    fun updateStart(value: LocalDateTime) { _state.value = _state.value.copy(startDateTime = value) }
    fun updateEnd(value: LocalDateTime?) { _state.value = _state.value.copy(endDateTime = value) }
    fun updateReminder(value: LocalDateTime?) { _state.value = _state.value.copy(reminderDateTime = value) }

    fun save() {
        val s = _state.value
        if (s.title.isBlank()) return
        viewModelScope.launch {
            repository.save(
                EventEntity(
                    id = s.id,
                    title = s.title.trim(),
                    notes = s.notes.trim(),
                    location = s.location.trim(),
                    startDateTime = s.startDateTime,
                    endDateTime = s.endDateTime,
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
                EventEntity(
                    id = s.id,
                    title = s.title,
                    notes = s.notes,
                    location = s.location,
                    startDateTime = s.startDateTime,
                    endDateTime = s.endDateTime,
                    reminderDateTime = s.reminderDateTime
                )
            )
            _state.value = _state.value.copy(deleted = true)
        }
    }
}

class EventEditViewModelFactory(
    private val application: Application,
    private val eventId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        EventEditViewModel(application, eventId) as T
}
