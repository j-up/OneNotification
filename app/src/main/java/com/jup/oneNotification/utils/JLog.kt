package com.jup.oneNotification.utils

import android.content.Context
import android.util.Log

class JLog {
    companion object {
        private const val TAG_NAME = "Jup"

        fun d(context: Context, msg: String) {
            Log.d(TAG_NAME, "${context.packageName} $msg")
        }
    }
}