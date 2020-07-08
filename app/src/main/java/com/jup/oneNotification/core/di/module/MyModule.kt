package com.jup.oneNotification.core.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jup.oneNotification.core.provider.LocationProvider
import com.jup.oneNotification.core.service.LocationWorker
import com.jup.oneNotification.utils.RequestPermission
import com.jup.oneNotification.view.dialog.TimePickerFragment
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val PREFERENCES_FILE_KEY = "com.jup.onenotification"

val appModule = module{
    single { provideSettingsPreferences(androidApplication()) }

    single { TimePickerFragment.newInstance(null,get()) }

    single { RequestPermission(it[0])}

    single { LocationWorker(androidApplication()) }
    single { LocationProvider(get()) }
}

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
