package com.farmdirect.app

import android.app.Application
import com.farmdirect.app.util.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FarmDirectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
