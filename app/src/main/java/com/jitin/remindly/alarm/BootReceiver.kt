package com.jitin.remindly.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jitin.remindly.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                rescheduleAll(appContext)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun rescheduleAll(context: Context) {
        val db = AppDatabase.getInstance(context)
        val scheduler = AlarmScheduler(context)
        val now = LocalDateTime.now()

        val tasks = db.taskDao().getAllSnapshot()
        tasks.filter { it.reminderDateTime != null && it.reminderDateTime.isAfter(now) && !it.isDone }
            .forEach { task ->
                scheduler.schedule(AlarmConstants.TYPE_TASK, task.id, task.title, task.notes, task.reminderDateTime!!)
            }

        val events = db.eventDao().getAllSnapshot()
        events.filter { it.reminderDateTime != null && it.reminderDateTime.isAfter(now) }
            .forEach { event ->
                scheduler.schedule(AlarmConstants.TYPE_EVENT, event.id, event.title, event.notes, event.reminderDateTime!!)
            }
    }
}
