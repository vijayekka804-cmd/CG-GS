package com.example.ads

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ui.theme.CgBorder
import com.example.ui.theme.CgSurface
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

enum class BannerType {
    BANNER_1,
    BANNER_2
}

/**
 * Reusable Jetpack Compose component for anchored adaptive Google AdMob banner ads.
 *
 * Automatically manages AdView lifecycle, calculates adaptive orientation widths,
 * gracefully collapses on failure, and avoids duplicate requests upon recomposition.
 */
@Composable
fun BannerAdView(
    bannerType: BannerType = BannerType.BANNER_1,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isInspection = LocalInspectionMode.current
    val lifecycleOwner = LocalLifecycleOwner.current

    if (isInspection) {
        // Preview placeholder
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp),
            color = CgSurface,
            shape = RoundedCornerShape(8.dp)
        ) {}
        return
    }

    val adUnitId = when (bannerType) {
        BannerType.BANNER_1 -> AdMobConfig.banner1AdUnitId
        BannerType.BANNER_2 -> AdMobConfig.banner2AdUnitId
    }

    var isAdLoaded by remember { mutableStateOf(false) }
    var isAdFailed by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .testTag(if (bannerType == BannerType.BANNER_1) "admob_banner_1" else "admob_banner_2"),
        contentAlignment = Alignment.Center
    ) {
        val widthDp = maxWidth.value.toInt().coerceAtLeast(320)

        // Only display container when ad is successfully loaded
        AnimatedVisibility(
            visible = isAdLoaded && !isAdFailed,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = CgSurface,
                shadowElevation = 1.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { ctx ->
                            val adSize = getAdaptiveAdSize(ctx, widthDp)
                            AdView(ctx).apply {
                                setAdSize(adSize)
                                setAdUnitId(adUnitId)
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )

                                adListener = object : AdListener() {
                                    override fun onAdLoaded() {
                                        isAdLoaded = true
                                        isAdFailed = false
                                        Log.d("BannerAdView", "Banner ad loaded successfully ($adUnitId)")
                                    }

                                    override fun onAdFailedToLoad(error: LoadAdError) {
                                        isAdLoaded = false
                                        isAdFailed = true
                                        Log.w("BannerAdView", "Banner ad failed to load ($adUnitId): ${error.message}")
                                    }
                                }

                                loadAd(AdRequest.Builder().build())
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Computes an anchored adaptive banner AdSize based on container width.
 */
private fun getAdaptiveAdSize(context: Context, widthDp: Int): AdSize {
    return try {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val density = displayMetrics.density
        val calculatedWidth = if (widthDp > 0) widthDp else (displayMetrics.widthPixels / density).toInt()
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, calculatedWidth)
    } catch (e: Exception) {
        AdSize.BANNER
    }
}
