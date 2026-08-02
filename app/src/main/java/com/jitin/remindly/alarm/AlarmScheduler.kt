package com.jitin.remindly.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_ITEM_ID
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_ITEM_TYPE
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_NOTES
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_TITLE
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun canScheduleExactAlarms(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) alarmManager.canScheduleExactAlarms() else true

    fun schedule(type: String, id: Long, title: String, notes: String, triggerAt: LocalDateTime) {
        val triggerAtMillis = triggerAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val pendingIntent = buildPendingIntent(type, id, title, notes)

        try {
            if (canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    fun cancel(type: String, id: Long) {
        val pendingIntent = findExistingPendingIntent(type, id) ?: return
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun buildPendingIntent(type: String, id: Long, title: String, notes: String): PendingIntent {
        val requestCode = AlarmConstants.requestCodeFor(type, id)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, requestCode, buildIntent(type, id, title, notes), flags)
    }

    private fun findExistingPendingIntent(type: String, id: Long): PendingIntent? {
        val requestCode = AlarmConstants.requestCodeFor(type, id)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        return PendingIntent.getBroadcast(context, requestCode, buildIntent(type, id, "", ""), flags)
    }

    private fun buildIntent(type: String, id: Long, title: String, notes: String): Intent =
        Intent(context, AlarmReceiver::class.java).apply {
            action = "$type-$id"
            putExtra(EXTRA_ITEM_ID, id)
            putExtra(EXTRA_ITEM_TYPE, type)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_NOTES, notes)
        }
}
