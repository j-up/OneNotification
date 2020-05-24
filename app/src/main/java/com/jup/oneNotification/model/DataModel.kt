package com.jup.oneNotification.model

data class AlarmDate(val hour: Int, val minute: Int) {
    override fun toString():String {
        return "$hour : $minute"
    }
}

class KeyData {
    companion object {

        val TIME_HOUR = "KEY_TIME_HOUR"
        val TIME_MINUTE = "KEY_TIME_MINUTE"

    }
}

