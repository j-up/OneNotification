package com.jup.oneNotification.core.provider

import com.jup.oneNotification.core.service.LocationWorker
import com.jup.oneNotification.model.LocationModel


class LocationProvider(private val locationWorker: LocationWorker) {

    fun onLocation():LocationModel {
        return locationWorker.getLocation()
    }

}
