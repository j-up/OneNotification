package com.jup.oneNotification.core.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jup.oneNotification.core.network.OpenWeather
import com.jup.oneNotification.core.provider.LocationProvider
import com.jup.oneNotification.core.service.LocationWorker
import com.jup.oneNotification.utils.PermissionUtil
import com.jup.oneNotification.view.dialog.TimePickerFragment
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val PREFERENCES_FILE_KEY = "com.jup.onenotification"
private const val OPENWEATHER_BASE_URL = "http://api.openweathermap.org/"

val appModule = module{
    single { provideSettingsPreferences(androidApplication()) }

    single { TimePickerFragment.newInstance(null,get()) }

    single { PermissionUtil(it[0])}

    single { LocationProvider(get()) }
    single { createOpenWeather() }
    single { LocationWorker(androidApplication()) }
}

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

private fun createOpenWeather(): OpenWeather {
    return Retrofit.Builder()
        .baseUrl(OPENWEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenWeather::class.java)
}