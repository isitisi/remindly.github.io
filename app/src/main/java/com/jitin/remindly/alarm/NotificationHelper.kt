package com.jitin.remindly.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.jitin.remindly.R
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_ITEM_ID
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_ITEM_TYPE
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_NOTES
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_TITLE

object NotificationHelper {

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(AlarmConstants.CHANNEL_ID) != null) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            AlarmConstants.CHANNEL_ID,
            context.getString(R.string.alarm_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.alarm_channel_description)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), audioAttributes)
            enableVibration(true)
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(channel)
    }

    fun showReminderNotification(context: Context, type: String, id: Long, title: String, notes: String) {
        ensureChannel(context)
        val requestCode = AlarmConstants.requestCodeFor(type, id)

        val fullScreenIntent = Intent(context, AlarmRingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            putExtra(EXTRA_ITEM_ID, id)
            putExtra(EXTRA_ITEM_TYPE, type)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_NOTES, notes)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, requestCode, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = AlarmConstants.ACTION_DISMISS
            putExtra(EXTRA_ITEM_ID, id)
            putExtra(EXTRA_ITEM_TYPE, type)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context, requestCode, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = AlarmConstants.ACTION_SNOOZE
            putExtra(EXTRA_ITEM_ID, id)
            putExtra(EXTRA_ITEM_TYPE, type)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_NOTES, notes)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context, requestCode + 500_000, snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, AlarmConstants.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(notes.ifBlank { context.getString(R.string.reminder_label) })
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setContentIntent(fullScreenPendingIntent)
            .setAutoCancel(true)
            .addAction(0, context.getString(R.string.dismiss), dismissPendingIntent)
            .addAction(0, context.getString(R.string.snooze), snoozePendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(requestCode, notification)
    }

    fun cancelNotification(context: Context, type: String, id: Long) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(AlarmConstants.requestCodeFor(type, id))
    }
}
