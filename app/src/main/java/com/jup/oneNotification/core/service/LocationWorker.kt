package com.jup.oneNotification.core.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.jup.oneNotification.model.LocationModel
import com.jup.oneNotification.utils.JLog
import java.lang.Exception
import java.util.*

class LocationWorker(private val context:Context) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    var addresses: List<Address> = emptyList()
    var location: Location? = null

    object LocationConst {
        const val NOT_PERMISSION = 483
        const val FAIL_GET_LOCATION = 484
        const val DISABLE_NETWORK_PROVIDER = 485
        const val SUCCESS_INIT_CHECK = 233
        const val SUCCESS_GET_LOCATION = 234
    }

    fun getLocation(): LocationModel {
        val checkResult = initCheck()

        if(checkResult!=LocationConst.SUCCESS_INIT_CHECK)
            return LocationModel(emptyList(),checkResult)

        try {
            addresses = geocoder.getFromLocation(
                location!!.latitude,
                location!!.longitude,
                1)
            JLog.d(this::class.java,addresses.toString())
        } catch (e: Exception) {
            JLog.e(this::class.java,e.message!!)
            return LocationModel(emptyList(),LocationConst.FAIL_GET_LOCATION)
        }

        return LocationModel(addresses,LocationConst.SUCCESS_GET_LOCATION)
    }

    private fun initCheck(): Int {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return LocationConst.NOT_PERMISSION
        }

        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            return LocationConst.DISABLE_NETWORK_PROVIDER

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            ?: return LocationConst.FAIL_GET_LOCATION

        return LocationConst.SUCCESS_INIT_CHECK
    }

}