package com.jup.oneNotification.core.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val PREFERENCES_FILE_KEY = "com.jup.onenotification"

val appModule = module{
    single { provideSettingsPreferences(androidApplication()) }
}


private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)