package com.jup.oneNotification.model

data class AlarmDate(val hour: Int, val minute: Int) {
    override fun toString():String {
        return "$hour : $minute"
    }
}