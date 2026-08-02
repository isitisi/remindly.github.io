package com.jitin.remindly.alarm

object AlarmConstants {
    const val CHANNEL_ID = "reminders_channel"

    const val EXTRA_ITEM_ID = "extra_item_id"
    const val EXTRA_ITEM_TYPE = "extra_item_type"
    const val EXTRA_TITLE = "extra_title"
    const val EXTRA_NOTES = "extra_notes"

    const val TYPE_TASK = "task"
    const val TYPE_EVENT = "event"

    const val ACTION_DISMISS = "com.jitin.remindly.action.DISMISS"
    const val ACTION_SNOOZE = "com.jitin.remindly.action.SNOOZE"

    const val SNOOZE_MINUTES = 10L

    /** Task and event reminders share the AlarmManager/notification request-code space,
     * so event alarm ids are offset to avoid colliding with task alarm ids. */
    const val EVENT_REQUEST_CODE_OFFSET = 1_000_000

    fun requestCodeFor(type: String, id: Long): Int {
        val base = (id % 1_000_000).toInt()
        return if (type == TYPE_EVENT) base + EVENT_REQUEST_CODE_OFFSET else base
    }
}
