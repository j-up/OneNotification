package com.jup.oneNotification.core.service

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import com.jup.oneNotification.utils.JLog
import java.io.IOException
import java.util.*

class FetchAddressIntentService : IntentService("Myservice") {
    //constructor(tag:String) : super("service")

    private var receiver: ResultReceiver? = null

    private fun deliverResultToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle().apply { putString(Constants.RESULT_DATA_KEY, message) }
        receiver?.send(resultCode, bundle)
    }

    override fun onHandleIntent(intent: Intent?) {
        val geocoder = Geocoder(this, Locale.getDefault())

        intent ?: return

        var errorMessage = ""

        // Get the location passed to this service through an extra.
        val location = intent.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)

        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                // In this sample, we get just a single address.
                1)
        } catch (ioException: IOException) {
            ioException.message?.let { JLog.d(this::class.java, it) }
        } catch (illegalArgumentException: IllegalArgumentException) {
            /*illegalArgumentException.message?.let { JLog.d(this::class.java, "error: $it \n Lat=${location.latitude} " +
                    "Lon=${location.longitude}") }*/
            illegalArgumentException.printStackTrace()
        }

        // Handle case where no address was found.
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                JLog.d(this::class.java, "address is empty")
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
        } else {
            val address = addresses[0]
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                addressFragments.joinToString(separator = "\n"))
        }
    }

    object Constants {
        const val SUCCESS_RESULT = 0
        const val FAILURE_RESULT = 1
        const val PACKAGE_NAME = "com.jup.oneNotification.core.service"
        const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_DATA_KEY = "${PACKAGE_NAME}.RESULT_DATA_KEY"
        const val LOCATION_DATA_EXTRA = "${PACKAGE_NAME}.LOCATION_DATA_EXTRA"
    }

}