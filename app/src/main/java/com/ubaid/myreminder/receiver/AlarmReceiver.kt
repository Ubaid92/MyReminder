package com.ubaid.myreminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "Reminder") {
            val message = intent.getStringExtra("title") ?: return
            Log.d("ALARM_RECEIVED_123", message)
        }
    }
}