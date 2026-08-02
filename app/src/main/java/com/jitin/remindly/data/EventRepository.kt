package com.jitin.remindly.data

import android.content.Context
import com.jitin.remindly.alarm.AlarmConstants
import com.jitin.remindly.alarm.AlarmScheduler
import kotlinx.coroutines.flow.Flow

class EventRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).eventDao()
    private val scheduler = AlarmScheduler(context)

    fun observeAll(): Flow<List<EventEntity>> = dao.getAll()

    suspend fun getById(id: Long): EventEntity? = dao.getById(id)

    suspend fun save(event: EventEntity): Long {
        val id = dao.upsert(event)
        val savedId = if (event.id == 0L) id else event.id
        syncAlarm(event.copy(id = savedId))
        return savedId
    }

    suspend fun delete(event: EventEntity) {
        dao.delete(event)
        scheduler.cancel(AlarmConstants.TYPE_EVENT, event.id)
    }

    private fun syncAlarm(event: EventEntity) {
        val reminder = event.reminderDateTime
        if (reminder != null) {
            scheduler.schedule(AlarmConstants.TYPE_EVENT, event.id, event.title, event.notes, reminder)
        } else {
            scheduler.cancel(AlarmConstants.TYPE_EVENT, event.id)
        }
    }
}
