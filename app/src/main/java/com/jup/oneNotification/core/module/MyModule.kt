package com.jup.oneNotification.core.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jup.oneNotification.core.provider.AddressProvider
import com.jup.oneNotification.view.dialog.TimePickerFragment
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREFERENCES_FILE_KEY = "com.jup.onenotification"

val appModule = module{
    single { provideSettingsPreferences(androidApplication()) }
    single { getAddressProvider(it[0]) }
    single { getTimPickerFragment(get()) }
}


private fun getTimPickerFragment(sharedPreferences: SharedPreferences) = TimePickerFragment.newInstance(null,sharedPreferences)

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

private fun getAddressProvider(context: Context) = AddressProvider(context)