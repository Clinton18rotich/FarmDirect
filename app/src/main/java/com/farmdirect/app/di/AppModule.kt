package com.farmdirect.app.di

import android.content.Context
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.ml.FeedbackCollector
import com.farmdirect.app.data.ml.ModelCache
import com.farmdirect.app.data.remote.VideoCallService
import com.farmdirect.app.domain.webrtc.WebRTCManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): FarmDirectPreferences {
        return FarmDirectPreferences(context)
    }
    
    @Provides
    @Singleton
    fun provideFeedbackCollector(): FeedbackCollector {
        return FeedbackCollector()
    }
    
    @Provides
    @Singleton
    fun provideModelCache(@ApplicationContext context: Context): ModelCache {
        return ModelCache(context)
    }
    
    @Provides
    @Singleton
    fun provideVideoCallService(): VideoCallService {
        return VideoCallService()
    }
    
    @Provides
    @Singleton
    fun provideWebRTCManager(@ApplicationContext context: Context): WebRTCManager {
        return WebRTCManager(context)
    }
}
