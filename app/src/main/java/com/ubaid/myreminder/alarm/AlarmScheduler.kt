package com.ubaid.myreminder.alarm

import com.ubaid.myreminder.data.ReminderData

interface AlarmScheduler {

    fun schedule (item: ReminderData)
    fun cancel (item: ReminderData)
}