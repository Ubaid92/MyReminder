package com.ubaid.myreminder.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.receiver.AlarmReceiver
import java.time.ZoneId

class AndroidAlarmScheduler (private val context: Context):AlarmScheduler{

    private  val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun schedule(item: ReminderData) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", item.title)
            putExtra("id", item.id)
            putExtra("priority", item.priority)
            putExtra("time", item.time)
        }
        intent.action = "Reminder"

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        )
    }

    override fun cancel(item: ReminderData) {
       alarmManager.cancel(
           PendingIntent.getBroadcast(
               context,
               item.hashCode(),
               Intent(context, AlarmReceiver::class.java),
               PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
           )
       )
    }


}