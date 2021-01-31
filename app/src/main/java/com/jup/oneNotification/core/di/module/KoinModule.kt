package com.jup.oneNotification.core.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jup.oneNotification.core.network.NewsApi
import com.jup.oneNotification.core.network.OpenWeatherApi
import com.jup.oneNotification.core.provider.LocationProvider
import com.jup.oneNotification.core.service.LocationWorker
import com.jup.oneNotification.utils.PermissionUtil
import com.jup.oneNotification.view.dialog.TimePickerFragment
import com.jup.oneNotification.viewModel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val PREFERENCES_FILE_KEY = "com.jup.onenotification"
private const val OPENWEATHER_BASE_URL = "http://api.openweathermap.org/"
private const val NEWS_BASE_URL = "http://newsapi.org/"

val appModule = module{
    single { provideSettingsPreferences(androidApplication()) }

    single { TimePickerFragment.newInstance(null,get()) }

    single { PermissionUtil(get())}

    single { LocationProvider(get()) }
    single { createOpenWeatherApi() }
    single { createNewsApi() }
    single { LocationWorker(androidApplication()) }

    viewModel { MainViewModel(get(),get(),get(),get(),get(),get()) }
}

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

private fun createOpenWeatherApi(): OpenWeatherApi {
    return Retrofit.Builder()
        .baseUrl(OPENWEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenWeatherApi::class.java)
}

private fun createNewsApi(): NewsApi {
    return Retrofit.Builder()
        .baseUrl(NEWS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)
}