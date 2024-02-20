package com.ubaid.myreminder.data

import java.util.UUID

data class ReminderData(
    var id:String = UUID.randomUUID().toString(),
    var isDone: Boolean,
    var title: String,
    var time: Long,
    var priority: Int,
){
    constructor():this("",false,  "", 0L,0)
}
