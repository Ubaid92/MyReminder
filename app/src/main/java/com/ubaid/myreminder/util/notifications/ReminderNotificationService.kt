package com.ubaid.myreminder.util.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ubaid.myreminder.MainActivity
import com.ubaid.myreminder.R
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.util.DateUtils


object ReminderNotificationService {
    private const val REMINDER_CHANNEL = "Reminders"
    const val PROMOTION_CHANNEL = "Promotions"

    fun notify(context: Context, reminderData: ReminderData) {

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL)
            .setContentTitle(reminderData.title)
            .setContentText(DateUtils.getFormattedTime(reminderData.time))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(createMainActivityIntent(context))
            .setSmallIcon(getPriorityIcon(reminderData.priority))
            .build()



        getNotificationManager(context)?.createNotificationChannel(
            NotificationChannel(REMINDER_CHANNEL, "Reminders", NotificationManager.IMPORTANCE_HIGH)
        )
        getNotificationManager(context)?.notify(Math.random().toInt(), notification)
    }

    private fun getNotificationManager(context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?


    private fun createMainActivityIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context,99,intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getPriorityIcon(priority: String): Int {
        return when (priority) {
            "High" -> R.drawable.bg_high
            "Medium" -> R.drawable.bg_med
            else -> R.drawable.bg_low
        }
    }
}