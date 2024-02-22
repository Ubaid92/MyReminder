package com.ubaid.myreminder.data.realtimedb

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.ubaid.myreminder.auth.AppAuthManager.Companion.TAG
import com.ubaid.myreminder.data.ReminderData

class FirebaseDb {
    private val database =
        FirebaseDatabase.getInstance("https://my-reminder-df003-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myRef = database.getReference("Reminders")

    fun saveToDatabase(reminderList: ArrayList<ReminderData>) {
        Firebase.auth.currentUser?.email?.let { userEmail ->
            myRef.child(userEmail.toKey())
                .setValue(reminderList)
        }
    }

    fun getData(callback: (ArrayList<ReminderData>) -> Unit) {
        val userEmail = Firebase.auth.currentUser?.email ?: return
        FirebaseDatabase.getInstance("https://my-reminder-df003-default-rtdb.asia-southeast1.firebasedatabase.app")
        myRef.child(userEmail.toKey()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<ArrayList<ReminderData>>()
                Log.d(TAG, "Value is: $value")
                callback(value ?: arrayListOf())
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun String.toKey():String{
        return this.replace("@gmail.com","").replace(".","")
    }
}