package com.ubaid.myreminder.data.realtimedb

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ubaid.myreminder.data.ReminderData

class FirebaseDb {
    private val database = FirebaseDatabase.getInstance("https://my-reminder-df003-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myRef = database.getReference("Reminders")

    fun saveToDatabase(reminderList:ArrayList<ReminderData>){
        Firebase.auth.currentUser?.email?.let {userEmail->
            myRef.child(userEmail.replace("@gmail.com",""))
                .setValue(reminderList)
        }
    }
}