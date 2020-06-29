package com.jup.oneNotification.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class RequestPermission(private val requiredPermissions:ArrayList<String>, private val context: Context) {

    private fun checkPermissions():ArrayList<String> {
        var rejectedPermissionList = ArrayList<String>()

        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissionList.add(permission)
            }
        }

        return rejectedPermissionList
       /* //거절된 퍼미션이 있다면...
        if(rejectedPermissionList.isNotEmpty()){
            //권한 요청!
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(context as Activity, rejectedPermissionList.toArray(array), multiplePermissionsCode)
        }*/
    }
}