package com.ubaid.myreminder

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ubaid.myreminder.alarm.AndroidAlarmScheduler
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.data.ReminderRepository
import java.util.Calendar

class ReminderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ReminderRepository()
    private val alarmScheduler = AndroidAlarmScheduler(application)
    val reminderTaskLiveData = MutableLiveData<ArrayList<ReminderData>>()
    val editReminder= MutableLiveData<ReminderData>()
    var selectedTime: Calendar = Calendar.getInstance()

    fun save(reminderData:ReminderData){
        repository.saveReminder(reminderData)
        reminderTaskLiveData.postValue(repository.getAllData())
    }
    fun update(reminderData: ReminderData) {
        updateScheduleStatus(reminderData)
        repository.update(reminderData)
        reminderTaskLiveData.postValue(repository.getAllData())
    }

    fun delete(reminderData: ReminderData){
        repository.deleteReminder(reminderData)
        reminderTaskLiveData.postValue(repository.getAllData())
    }

    fun edit(reminderData: ReminderData){
        repository.editReminder(reminderData)
        reminderTaskLiveData.postValue(repository.getAllData())
    }

    private fun updateScheduleStatus(reminderData: ReminderData) {
        if (reminderData.isDone) {
            alarmScheduler.cancel(reminderData)
        } else {
            alarmScheduler.schedule(reminderData)
        }
    }

    fun getDataFromId(){
        repository.getDataAsPerEmail{
            reminderTaskLiveData.postValue(it)
        }
    }

//    fun getOnlyTodayData(reminderData: ReminderData){
//        var time = reminderData.time
//        DateUtils.isToday(time)
//    }



}