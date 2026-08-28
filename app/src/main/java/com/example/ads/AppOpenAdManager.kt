package com.example.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

/**
 * Lifecycle-aware manager for Google AdMob App Open Ads.
 *
 * Tracks app foreground transitions, enforces ad freshness (4 hours limit),
 * ensures full-screen ad mutual exclusion, and prevents disruption during study sessions.
 */
class AppOpenAdManager(
    private val application: Application
) : Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    companion object {
        private const val TAG = "AppOpenAdManager"
        private const val FOUR_HOURS_MILLIS = 4 * 3600 * 1000L
        private var instance: AppOpenAdManager? = null

        // Flag indicating whether active screen is viewing a PDF document (prevent app open interruption)
        @Volatile
        var isReadingPdf: Boolean = false

        fun init(application: Application): AppOpenAdManager {
            return instance ?: synchronized(this) {
                instance ?: AppOpenAdManager(application).also {
                    it.register()
                    instance = it
                }
            }
        }
    }

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd: Boolean = false
    private var loadTime: Long = 0L
    private var currentActivity: Activity? = null
    private var isColdStart: Boolean = true

    private fun register() {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Checks if ad exists and is still valid (less than 4 hours old).
     */
    private fun isAdAvailable(): Boolean {
        val isValid = appOpenAd != null && (Date().time - loadTime < FOUR_HOURS_MILLIS)
        return isValid
    }

    /**
     * Loads a fresh App Open Ad.
     */
    fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true
        val request = AdRequest.Builder().build()
        val adUnitId = AdMobConfig.appOpenAdUnitId

        AppOpenAd.load(
            context.applicationContext,
            adUnitId,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                    Log.d(TAG, "App Open Ad loaded successfully ($adUnitId)")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    appOpenAd = null
                    isLoadingAd = false
                    Log.w(TAG, "App Open Ad failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    /**
     * Shows the App Open Ad if available and conditions are safe.
     */
    fun showAdIfAvailable(activity: Activity, onShowComplete: () -> Unit = {}) {
        if (AdManager.isFullScreenAdShowing) {
            Log.d(TAG, "Another full-screen ad is already showing. Skipping App Open Ad.")
            onShowComplete()
            return
        }

        if (isReadingPdf) {
            Log.d(TAG, "User is reading study PDF. Skipping App Open Ad to preserve study focus.")
            onShowComplete()
            return
        }

        if (!isAdAvailable()) {
            Log.d(TAG, "App Open Ad is not ready or expired. Loading fresh one.")
            onShowComplete()
            loadAd(activity)
            return
        }

        val ad = appOpenAd ?: run {
            onShowComplete()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                AdManager.isFullScreenAdShowing = true
                Log.d(TAG, "App Open Ad shown on screen.")
            }

            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                AdManager.isFullScreenAdShowing = false
                Log.d(TAG, "App Open Ad dismissed.")
                loadAd(activity)
                onShowComplete()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                AdManager.isFullScreenAdShowing = false
                Log.w(TAG, "App Open Ad failed to show: ${adError.message}")
                loadAd(activity)
                onShowComplete()
            }
        }

        try {
            ad.show(activity)
        } catch (e: Exception) {
            Log.e(TAG, "Exception showing App Open Ad", e)
            appOpenAd = null
            AdManager.isFullScreenAdShowing = false
            loadAd(activity)
            onShowComplete()
        }
    }

    // Process Lifecycle Observer - Triggers on app foreground
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Skip ad display on initial cold start so users enter the app immediately without delay
        if (isColdStart) {
            isColdStart = false
            currentActivity?.let { loadAd(it) }
            return
        }

        currentActivity?.let { activity ->
            if (!activity.isFinishing && !activity.isDestroyed) {
                showAdIfAvailable(activity)
            }
        }
    }

    // Activity Lifecycle Callbacks
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }
}
