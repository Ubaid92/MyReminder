package com.ubaid.myreminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.util.notifications.ReminderNotificationService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "Reminder") {
            val message = intent.getStringExtra("title") ?: return
            Log.d("ALARM_RECEIVED_123", message)

            val id = intent.getStringExtra("id") ?: return
            val priority = intent.getStringExtra("priority") ?: return
            val time = intent.getLongExtra("time", 0L) ?: return
            context?.let {
                ReminderNotificationService.notify(
                    it, ReminderData(
                        id, false, message, time,
                        priority, true
                    )
                )
            }

        }
    }
}