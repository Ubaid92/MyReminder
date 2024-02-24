package com.ubaid.myreminder.util

import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    fun getFormattedTime(timestamp: Long): String? {
        return SimpleDateFormat("hh:mm a", Locale.US).format(timestamp)
    }

    fun getFormattedDate(
        calendar: Calendar
    ): String? {
        return SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(calendar.time)
    }
}