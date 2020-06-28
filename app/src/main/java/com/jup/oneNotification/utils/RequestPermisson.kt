package com.jup.oneNotification.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class RequestPermisson(private val requiredPermissions:ArrayList<String>, private val multiplePermissionsCode: Int) {


    private fun checkPermissions():ArrayList<String> {
        //거절되었거나 아직 수락하지 않은 권한(퍼미션)을 저장할 문자열 배열 리스트
        var rejectedPermissionList = ArrayList<String>()

        /*for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissionList.add(permission)
            }
        }*/
       /* //거절된 퍼미션이 있다면...
        if(rejectedPermissionList.isNotEmpty()){
            //권한 요청!
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(context as Activity, rejectedPermissionList.toArray(array), multiplePermissionsCode)
        }*/
    }
}