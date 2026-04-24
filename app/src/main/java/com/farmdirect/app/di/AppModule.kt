package com.farmdirect.app.di

import android.content.Context
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.local.OfflineManager
import com.farmdirect.app.data.ml.FeedbackCollector
import com.farmdirect.app.data.ml.ModelCache
import com.farmdirect.app.data.remote.NewsFeedService
import com.farmdirect.app.data.remote.VideoCallService
import com.farmdirect.app.data.remote.admin.CreatorDashboardAPI
import com.farmdirect.app.domain.ml.AILanguageLearner
import com.farmdirect.app.domain.ml.LocalLanguageEngine
import com.farmdirect.app.domain.webrtc.WebRTCManager
import com.farmdirect.app.util.AchievementSystem
import com.farmdirect.app.util.AppRatingManager
import com.farmdirect.app.util.AppShortcutsManager
import com.farmdirect.app.util.ARCropMeasurement
import com.farmdirect.app.util.BiometricAuthManager
import com.farmdirect.app.util.DataExportService
import com.farmdirect.app.util.HapticFeedbackManager
import com.farmdirect.app.util.LiveLocationSharing
import com.farmdirect.app.util.NetworkErrorHandler
import com.farmdirect.app.util.QRCodeManager
import com.farmdirect.app.util.ReferralManager
import com.farmdirect.app.util.SoundEffectsManager
import com.farmdirect.app.util.VoiceCommandManager
import com.farmdirect.app.util.VoiceNoteManager
import com.farmdirect.app.util.WhatsAppIntegration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ═══════════════════════════════════════
    // 1. CORE SERVICES
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): FarmDirectPreferences {
        return FarmDirectPreferences(context)
    }

    // ═══════════════════════════════════════
    // 2. OFFLINE & SYNC
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideOfflineManager(@ApplicationContext context: Context): OfflineManager {
        return OfflineManager(context)
    }

    // ═══════════════════════════════════════
    // 3. AI & MACHINE LEARNING
    // ═══════════════════════════════════════
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
    fun provideLocalLanguageEngine(@ApplicationContext context: Context): LocalLanguageEngine {
        return LocalLanguageEngine(context)
    }

    @Provides
    @Singleton
    fun provideAILanguageLearner(@ApplicationContext context: Context): AILanguageLearner {
        return AILanguageLearner(context)
    }

    // ═══════════════════════════════════════
    // 4. VIDEO CALLING
    // ═══════════════════════════════════════
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

    // ═══════════════════════════════════════
    // 5. GROWTH & ENGAGEMENT
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideReferralManager(@ApplicationContext context: Context): ReferralManager {
        return ReferralManager(context)
    }

    @Provides
    @Singleton
    fun provideAchievementSystem(@ApplicationContext context: Context): AchievementSystem {
        return AchievementSystem(context)
    }

    @Provides
    @Singleton
    fun provideAppRatingManager(@ApplicationContext context: Context): AppRatingManager {
        return AppRatingManager(context)
    }

    // ═══════════════════════════════════════
    // 6. SECURITY
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideBiometricAuthManager(@ApplicationContext context: Context): BiometricAuthManager {
        return BiometricAuthManager(context)
    }

    // ═══════════════════════════════════════
    // 7. SMART FEATURES
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideVoiceCommandManager(@ApplicationContext context: Context): VoiceCommandManager {
        return VoiceCommandManager(context)
    }

    @Provides
    @Singleton
    fun provideARCropMeasurement(@ApplicationContext context: Context): ARCropMeasurement {
        return ARCropMeasurement(context)
    }

    @Provides
    @Singleton
    fun provideQRCodeManager(@ApplicationContext context: Context): QRCodeManager {
        return QRCodeManager(context)
    }

    @Provides
    @Singleton
    fun provideVoiceNoteManager(@ApplicationContext context: Context): VoiceNoteManager {
        return VoiceNoteManager(context)
    }

    // ═══════════════════════════════════════
    // 8. CONTENT & DATA
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideNewsFeedService(): NewsFeedService {
        return NewsFeedService()
    }

    @Provides
    @Singleton
    fun provideDataExportService(@ApplicationContext context: Context): DataExportService {
        return DataExportService(context)
    }

    // ═══════════════════════════════════════
    // 9. COMMUNICATION & NETWORK
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideWhatsAppIntegration(@ApplicationContext context: Context): WhatsAppIntegration {
        return WhatsAppIntegration(context)
    }

    @Provides
    @Singleton
    fun provideNetworkErrorHandler(@ApplicationContext context: Context): NetworkErrorHandler {
        return NetworkErrorHandler(context)
    }

    // ═══════════════════════════════════════
    // 10. LOCATION SERVICES
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideLiveLocationSharing(@ApplicationContext context: Context): LiveLocationSharing {
        return LiveLocationSharing(context)
    }

    // ═══════════════════════════════════════
    // 11. UX & FEEDBACK
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideHapticFeedbackManager(@ApplicationContext context: Context): HapticFeedbackManager {
        return HapticFeedbackManager(context)
    }

    @Provides
    @Singleton
    fun provideSoundEffectsManager(@ApplicationContext context: Context): SoundEffectsManager {
        return SoundEffectsManager(context)
    }

    // ═══════════════════════════════════════
    // 12. SYSTEM & ADMIN
    // ═══════════════════════════════════════
    @Provides
    @Singleton
    fun provideAppShortcutsManager(@ApplicationContext context: Context): AppShortcutsManager {
        return AppShortcutsManager(context)
    }

    @Provides
    @Singleton
    fun provideCreatorDashboardAPI(@ApplicationContext context: Context): CreatorDashboardAPI {
        return CreatorDashboardAPI(context)
    }
}
