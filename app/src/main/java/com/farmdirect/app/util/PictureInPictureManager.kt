package com.farmdirect.app.util

import android.app.Activity
import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational

object PictureInPictureManager {
    
    fun enterPiPMode(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .build()
            activity.enterPictureInPictureMode(params)
        }
    }
    
    fun isInPiPMode(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity.isInPictureInPictureMode
        } else false
    }
}
