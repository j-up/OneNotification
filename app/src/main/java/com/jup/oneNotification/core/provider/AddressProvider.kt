package com.jup.oneNotification.core.provider

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import com.jup.oneNotification.core.service.FetchAddressIntentService
import com.jup.oneNotification.utils.JLog

class AddressProvider(val context: Context) {
    private var lastLocation: Location? = null

    private val receiver = object: ResultReceiver(Handler()) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            val addressOutput = resultData?.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY) ?: ""
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                JLog.d(this::class.java,addressOutput)
            }

        }
    }

    fun startIntentService() {
        val intent = Intent(context, FetchAddressIntentService::class.java).apply {
            putExtra(FetchAddressIntentService.Constants.RECEIVER, receiver)
            putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, lastLocation)
        }
        context.startService(intent)
    }
}
