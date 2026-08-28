package com.example.ads

import com.example.BuildConfig

/**
 * Centralized configuration for all Google AdMob Ad Unit IDs and settings.
 *
 * Automatically switches between Google's official test ad unit IDs (for DEBUG)
 * and production ad unit IDs (for RELEASE / PRODUCTION).
 */
object AdMobConfig {

    // Set to true to force test ads even in release builds if desired for testing
    var forceTestAds: Boolean = false

    val isTestMode: Boolean
        get() = BuildConfig.DEBUG || forceTestAds

    // ==========================================
    // PRODUCTION AD UNIT IDS
    // ==========================================
    private const val PROD_BANNER_1 = "ca-app-pub-7994338654022536/7293967350"
    private const val PROD_BANNER_2 = "ca-app-pub-7994338654022536/1430327236"
    private const val PROD_INTERSTITIAL = "ca-app-pub-7994338654022536/8195985482"
    private const val PROD_NATIVE_ADVANCED = "ca-app-pub-7994338654022536/4776499708"
    private const val PROD_APP_OPEN = "ca-app-pub-7994338654022536/6911708451"

    // ==========================================
    // GOOGLE TEST AD UNIT IDS (FOR DEVELOPMENT)
    // ==========================================
    private const val TEST_BANNER = "ca-app-pub-3940256099942544/9214589741"
    private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_NATIVE_ADVANCED = "ca-app-pub-3940256099942544/2247696110"
    private const val TEST_APP_OPEN = "ca-app-pub-3940256099942544/9257395921"

    /**
     * Banner 1: Used on Home Screen and primary category browsing screens.
     */
    val banner1AdUnitId: String
        get() = if (isTestMode) TEST_BANNER else PROD_BANNER_1

    /**
     * Banner 2: Used on chapter and secondary list screens.
     */
    val banner2AdUnitId: String
        get() = if (isTestMode) TEST_BANNER else PROD_BANNER_2

    /**
     * Interstitial Ad: Used at natural transition points (e.g., after reading multiple chapters).
     */
    val interstitialAdUnitId: String
        get() = if (isTestMode) TEST_INTERSTITIAL else PROD_INTERSTITIAL

    /**
     * Native Advanced Ad: Used embedded within chapter lists with clear sponsored disclosure.
     */
    val nativeAdUnitId: String
        get() = if (isTestMode) TEST_NATIVE_ADVANCED else PROD_NATIVE_ADVANCED

    /**
     * App Open Ad: Used during app foreground transitions after backgrounding.
     */
    val appOpenAdUnitId: String
        get() = if (isTestMode) TEST_APP_OPEN else PROD_APP_OPEN

    // Interstitial frequency configuration
    const val INTERSTITIAL_MIN_INTERVAL_MS = 60_000L // Min 60s between interstitials
    const val ACTIONS_BETWEEN_INTERSTITIALS = 3     // Show at most every 3rd chapter/PDF return
}
