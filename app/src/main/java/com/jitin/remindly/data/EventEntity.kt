package com.jitin.remindly.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val location: String = "",
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime? = null,
    val reminderDateTime: LocalDateTime? = null
)
