package com.ubaid.myreminder.data

import com.ubaid.myreminder.data.realtimedb.FirebaseDb

class ReminderRepository {
    private val reminderList = arrayListOf<ReminderData>()

    private var firebaseDb = FirebaseDb()

    fun saveReminder(reminderData: ReminderData) {
        reminderList.add(reminderData)
        firebaseDb.saveToDatabase(reminderList)
    }

    fun deleteReminder(reminderData: ReminderData) {
        reminderList.remove(reminderData)
        firebaseDb.saveToDatabase(reminderList)
    }

    fun editReminder(reminderData: ReminderData) {
        val index= reminderList.indexOfFirst { it.id == reminderData.id }
        reminderList[index] = reminderData
        firebaseDb.saveToDatabase(reminderList)
    }

    fun update(reminderData: ReminderData) {
        val itemFromList = reminderList.find { it.id == reminderData.id }
        itemFromList?.isDone = reminderData.isDone
        itemFromList?.isAlert = reminderData.isAlert
        firebaseDb.saveToDatabase(reminderList)
    }

    fun getAllData() = reminderList

    fun getDataAsPerEmail(callback: (ArrayList<ReminderData>) -> Unit) {
        firebaseDb.getData {
            reminderList.clear()
            reminderList.addAll(it)
            callback(it)
        }
    }

//    fun getTodayData() {
//        reminderList.filter {
////            it.time
//        }
//    }
}