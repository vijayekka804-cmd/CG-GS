package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Central AdManager coordinating Google Mobile Ads SDK initialization,
 * interstitial lifecycle, full-screen concurrency control, and frequency caps.
 */
object AdManager {

    private const val TAG = "AdManager"

    private val isInitialized = AtomicBoolean(false)

    // Concurrency lock ensuring only one full-screen ad (Interstitial or App Open) is presented at a time
    @Volatile
    var isFullScreenAdShowing: Boolean = false

    // Interstitial Ad State
    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialLoading: Boolean = false
    private var lastInterstitialShowTime: Long = 0L
    private var transitionActionCount: Int = 0

    /**
     * Initialize the Google Mobile Ads SDK once at application startup.
     */
    fun initialize(context: Context, onInitialized: (() -> Unit)? = null) {
        if (isInitialized.compareAndSet(false, true)) {
            try {
                MobileAds.initialize(context.applicationContext) { initializationStatus ->
                    Log.d(TAG, "AdMob MobileAds initialized successfully: $initializationStatus")
                    preloadInterstitial(context.applicationContext)
                    onInitialized?.invoke()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing MobileAds SDK", e)
            }
        }
    }

    /**
     * Preloads an Interstitial ad in advance so it is ready for natural transition points.
     */
    fun preloadInterstitial(context: Context) {
        if (interstitialAd != null || isInterstitialLoading) {
            return
        }

        isInterstitialLoading = true
        val adRequest = AdRequest.Builder().build()
        val adUnitId = AdMobConfig.interstitialAdUnitId

        InterstitialAd.load(
            context.applicationContext,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isInterstitialLoading = false
                    Log.d(TAG, "Interstitial ad loaded successfully ($adUnitId)")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isInterstitialLoading = false
                    Log.w(TAG, "Interstitial ad failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    /**
     * Records a user transition action (e.g. returning from PDF or navigating chapters).
     * If the action threshold and cooldown period are met, displays the interstitial.
     * Always executes [onComplete] immediately without blocking if the ad is not ready.
     */
    fun onUserTransition(activity: Activity?, onComplete: () -> Unit = {}) {
        transitionActionCount++
        val now = System.currentTimeMillis()
        val elapsedSinceLastShow = now - lastInterstitialShowTime

        val canShow = transitionActionCount >= AdMobConfig.ACTIONS_BETWEEN_INTERSTITIALS &&
                elapsedSinceLastShow >= AdMobConfig.INTERSTITIAL_MIN_INTERVAL_MS &&
                interstitialAd != null &&
                !isFullScreenAdShowing

        if (canShow && activity != null && !activity.isFinishing && !activity.isDestroyed) {
            showInterstitial(activity, onComplete)
        } else {
            // If ad not shown, pre-load if needed and proceed immediately
            if (activity != null) {
                preloadInterstitial(activity)
            }
            onComplete()
        }
    }

    /**
     * Shows the loaded Interstitial ad with complete lifecycle management.
     */
    private fun showInterstitial(activity: Activity, onDismissedOrSkipped: () -> Unit) {
        val currentAd = interstitialAd
        if (currentAd == null || isFullScreenAdShowing) {
            onDismissedOrSkipped()
            return
        }

        currentAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                isFullScreenAdShowing = true
                lastInterstitialShowTime = System.currentTimeMillis()
                transitionActionCount = 0
                interstitialAd = null
                Log.d(TAG, "Interstitial displayed on screen.")
            }

            override fun onAdDismissedFullScreenContent() {
                isFullScreenAdShowing = false
                Log.d(TAG, "Interstitial dismissed by user.")
                preloadInterstitial(activity)
                onDismissedOrSkipped()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                isFullScreenAdShowing = false
                interstitialAd = null
                Log.w(TAG, "Interstitial failed to show: ${adError.message}")
                preloadInterstitial(activity)
                onDismissedOrSkipped()
            }
        }

        try {
            currentAd.show(activity)
        } catch (e: Exception) {
            Log.e(TAG, "Exception showing interstitial ad", e)
            isFullScreenAdShowing = false
            interstitialAd = null
            preloadInterstitial(activity)
            onDismissedOrSkipped()
        }
    }
}
