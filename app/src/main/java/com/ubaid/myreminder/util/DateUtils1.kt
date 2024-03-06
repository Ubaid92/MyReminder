package com.ubaid.myreminder.util

import android.icu.text.SimpleDateFormat
import java.util.Locale

object DateUtils1 {
    fun getFormattedTime(timestamp: Long): String? {
        return SimpleDateFormat("hh:mm a", Locale.US).format(timestamp)
    }

    fun getFormattedDate(
        calendar: Long
    ): String? {
        return SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(calendar)
    }

    fun getFormattedTimeAndDate(timestamp: Long):String?{

       return SimpleDateFormat("yyyy-MM-dd---hh:mm:ss a", Locale.US).format(timestamp)
    }

}