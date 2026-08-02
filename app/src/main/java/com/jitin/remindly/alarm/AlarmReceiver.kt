package com.jitin.remindly.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_ITEM_ID
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_ITEM_TYPE
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_NOTES
import com.jitin.remindly.alarm.AlarmConstants.EXTRA_TITLE

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(EXTRA_ITEM_ID, -1L)
        val type = intent.getStringExtra(EXTRA_ITEM_TYPE) ?: return
        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val notes = intent.getStringExtra(EXTRA_NOTES).orEmpty()
        if (id < 0) return

        NotificationHelper.showReminderNotification(context, type, id, title, notes)
    }
}
