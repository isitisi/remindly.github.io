package com.jitin.remindly.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun fromEpochMillis(value: Long?): LocalDateTime? =
        value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()) }

    @TypeConverter
    fun toEpochMillis(dateTime: LocalDateTime?): Long? =
        dateTime?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun fromPriority(value: String?): Priority =
        value?.let { Priority.valueOf(it) } ?: Priority.MEDIUM

    @TypeConverter
    fun toPriority(priority: Priority?): String = (priority ?: Priority.MEDIUM).name
}
