package com.jitin.remindly

import android.app.Application
import com.jitin.remindly.alarm.NotificationHelper

class RemindlyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.ensureChannel(this)
    }
}
