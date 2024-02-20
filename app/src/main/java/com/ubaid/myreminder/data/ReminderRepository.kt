package com.ubaid.myreminder.data

import com.ubaid.myreminder.data.realtimedb.FirebaseDb

class ReminderRepository {
    private val reminderList = arrayListOf<ReminderData>()

    var firebaseDb = FirebaseDb()

    fun saveReminder(reminderData: ReminderData) {
        reminderList.add(reminderData)
        firebaseDb.saveToDatabase(reminderList)
    }

    fun update(reminderData: ReminderData) {
        reminderList.find { it.id == reminderData.id }?.isDone = reminderData.isDone
        firebaseDb.saveToDatabase(reminderList)
    }
    fun getAllData()=reminderList

//    fun getTodayData() {
//        reminderList.filter {
////            it.time
//        }
//    }
}