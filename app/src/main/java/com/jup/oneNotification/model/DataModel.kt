package com.jup.oneNotification.model

data class AlarmDate(val hour: Int, val minute: Int) {
    override fun toString():String {
        return "$hour : $minute"
    }

    fun timeToAmPm():String {
        val ampm =
            if (hour <= 11) "오전"
            else "오후"

        val customHour =
            when(hour) {
                0 -> "12"
                in 13..24 -> hour-12
                else -> hour
            }

        return "$ampm $customHour 시 $minute 분 "
    }
}

class KeyData {
    companion object {
        const val KEY_TIME_HOUR = "KEY_TIME_HOUR"
        const val KEY_TIME_MINUTE = "KEY_TIME_MINUTE"

        const val KEY_WEATHER = "KEY_WEATHER"
        const val VALUE_WEATHER_OPEN_WEATHER = 2001
        const val VALUE_WEATHER_KOREA_WEATHER = 2002

        const val KEY_NEWS_CHO = "KEY_NEWS_CHO"
        const val KEY_NEWS_KHAN = "KEY_NEWS_KHAN"

    }
}

