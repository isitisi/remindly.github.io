package com.jitin.remindly.data

import android.content.Context
import com.jitin.remindly.alarm.AlarmConstants
import com.jitin.remindly.alarm.AlarmScheduler
import kotlinx.coroutines.flow.Flow

class TaskRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).taskDao()
    private val scheduler = AlarmScheduler(context)

    fun observeAll(): Flow<List<TaskEntity>> = dao.getAll()

    suspend fun getById(id: Long): TaskEntity? = dao.getById(id)

    suspend fun save(task: TaskEntity): Long {
        val id = dao.upsert(task)
        val savedId = if (task.id == 0L) id else task.id
        syncAlarm(task.copy(id = savedId))
        return savedId
    }

    suspend fun delete(task: TaskEntity) {
        dao.delete(task)
        scheduler.cancel(AlarmConstants.TYPE_TASK, task.id)
    }

    suspend fun setDone(task: TaskEntity, isDone: Boolean) {
        val updated = task.copy(isDone = isDone)
        dao.update(updated)
        syncAlarm(updated)
    }

    private fun syncAlarm(task: TaskEntity) {
        val reminder = task.reminderDateTime
        if (reminder != null && !task.isDone) {
            scheduler.schedule(AlarmConstants.TYPE_TASK, task.id, task.title, task.notes, reminder)
        } else {
            scheduler.cancel(AlarmConstants.TYPE_TASK, task.id)
        }
    }
}
