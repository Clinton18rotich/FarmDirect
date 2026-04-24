package com.farmdirect.app.util

object Constants {
    const val BASE_URL = "https://api.farmdirect.africa/v1/"
    const val TIMEOUT = 60L
    const val DEFAULT_CURRENCY = "KES"
    const val PLATFORM_FEE_PERCENTAGE = 3.0

    object Preferences {
        const val NAME = "farmdirect_prefs"
        const val KEY_TOKEN = "auth_token"
        const val KEY_USER_ID = "user_id"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    }
}
