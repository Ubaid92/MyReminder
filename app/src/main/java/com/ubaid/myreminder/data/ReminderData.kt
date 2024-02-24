package com.ubaid.myreminder.data

import java.util.UUID

data class ReminderData(
    var id: String = UUID.randomUUID().toString(),
    var isDone: Boolean,
    var title: String,
    var time: Long,
    var priority: String,
    var isAlert:Boolean,
) {
    constructor() : this("", false, "", 0L, "", false)
}
