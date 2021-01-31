package com.jup.oneNotification

import android.app.Application
import org.koin.android.ext.koin.androidContext
import com.jup.oneNotification.core.di.module.appModule
import org.koin.core.context.startKoin

class NotificationApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            androidContext(this@NotificationApplication)
            modules(appModule)
        }
    }

}