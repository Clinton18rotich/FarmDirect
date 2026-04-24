package com.farmdirect.app.util

import android.app.Activity
import android.os.Build
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

object SplashScreenAPI {
    
    fun install(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = activity.installSplashScreen()
            splashScreen.setKeepOnScreenCondition { false }
        }
    }
}
