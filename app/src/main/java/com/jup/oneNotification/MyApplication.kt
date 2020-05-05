package com.jup.oneNotification

import android.app.Application
import com.jup.oneNotification.core.module.appModule
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            modules(appModule)
        }
    }

}