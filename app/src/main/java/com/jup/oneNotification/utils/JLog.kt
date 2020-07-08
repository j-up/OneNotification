package com.jup.oneNotification.utils

import android.util.Log

class JLog {
    companion object {
        private const val TAG_NAME = "Jup"

        fun<T> e(myClass:  Class<T>, msg: String) {
            Log.e(TAG_NAME, "${myClass.simpleName} : $msg")
        }

        fun<T> d(myClass:  Class<T>, msg: String) {
            Log.d(TAG_NAME, "${myClass.simpleName} : $msg")
        }

        fun<T> v(myClass: Class<T>, msg: String) {
            Log.v(TAG_NAME, "${myClass.simpleName} : $msg")
        }
    }
}