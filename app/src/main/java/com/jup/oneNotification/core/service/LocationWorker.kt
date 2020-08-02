package com.jup.oneNotification.core.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.core.app.ActivityCompat
import com.jup.oneNotification.model.LocationModel
import com.jup.oneNotification.utils.JLog
import java.util.*


class LocationWorker(private val context:Context) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    private var addressList: List<Address> = emptyList()
    private var location: Location? = null

    object LocationConst {
        const val NOT_PERMISSION = 483
        const val FAIL_GET_LOCATION = 484
        const val DISABLE_NETWORK_PROVIDER = 485
        const val DISABLE_GPS_PROVIDER = 486

        const val SUCCESS_INIT_CHECK = 233
        const val SUCCESS_GET_LOCATION = 234
    }

    fun getLocation(): LocationModel {
        val checkStatus = initCheck()
        if(checkStatus!=LocationConst.SUCCESS_INIT_CHECK)
            return LocationModel(null,checkStatus)

        try {
            addressList = geocoder.getFromLocation(
                location!!.latitude,
                location!!.longitude,
                1)
            JLog.d(this::class.java,addressList.toString())
        } catch (e: Exception) {
            JLog.e(this::class.java,e.message!!)
            return LocationModel(null,LocationConst.FAIL_GET_LOCATION)
        }
        if(addressList.isEmpty())
            return LocationModel(null,LocationConst.FAIL_GET_LOCATION)

        return LocationModel(addressList[0],LocationConst.SUCCESS_GET_LOCATION)
    }

    private fun initCheck(): Int {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return LocationConst.NOT_PERMISSION
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return LocationConst.DISABLE_GPS_PROVIDER

        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            return LocationConst.DISABLE_NETWORK_PROVIDER

         if(location==null) {
             location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                 ?:return LocationConst.FAIL_GET_LOCATION
         }

        return LocationConst.SUCCESS_INIT_CHECK
    }

}