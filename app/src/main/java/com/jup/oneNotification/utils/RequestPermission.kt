package com.jup.oneNotification.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class RequestPermission(private val context: Context) {

    fun checkPermissions(requiredPermissions:ArrayList<String>):ArrayList<String> {
        var rejectedPermissionList = ArrayList<String>()

        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissionList.add(permission)
            }
        }

        return rejectedPermissionList

    }
}