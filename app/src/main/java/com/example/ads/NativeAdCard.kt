package com.example.ads

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.CgBluePrimary
import com.example.ui.theme.CgBorder
import com.example.ui.theme.CgSurface
import com.example.ui.theme.CgTextMuted
import com.example.ui.theme.CgTextPrimary
import com.example.ui.theme.CgTextSecondary
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * Reusable Native Advanced Ad Card styled seamlessly with the CG GS Material 3 design system.
 *
 * Includes a distinct "Ad / प्रायोजित" badge to ensure clear disclosure,
 * manages NativeAd lifecycle with destroy() on disposal, and gracefully collapses on failure.
 */
@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isInspection = LocalInspectionMode.current

    if (isInspection) {
        return
    }

    var loadedNativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isFailed by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, AdMobConfig.nativeAdUnitId)
            .forNativeAd { nativeAd ->
                loadedNativeAd?.destroy() // Release any prior ad
                loadedNativeAd = nativeAd
                isFailed = false
                Log.d("NativeAdCard", "Native ad loaded successfully (${AdMobConfig.nativeAdUnitId})")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isFailed = true
                    Log.w("NativeAdCard", "Native ad failed to load: ${error.message}")
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            loadedNativeAd?.destroy()
            loadedNativeAd = null
        }
    }

    AnimatedVisibility(
        visible = loadedNativeAd != null && !isFailed,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        loadedNativeAd?.let { nativeAd ->
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .testTag("admob_native_ad_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CgSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                AndroidView(
                    factory = { ctx ->
                        createNativeAdView(ctx, nativeAd)
                    },
                    update = { view ->
                        populateNativeAdView(nativeAd, view)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun createNativeAdView(context: Context, nativeAd: NativeAd): NativeAdView {
    val nativeAdView = NativeAdView(context)

    val container = android.widget.LinearLayout(context).apply {
        orientation = android.widget.LinearLayout.VERTICAL
        setPadding(dpToPx(context, 16), dpToPx(context, 14), dpToPx(context, 16), dpToPx(context, 14))
        layoutParams = android.widget.FrameLayout.LayoutParams(
            android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
            android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

    // Top Header: Badge & Advertiser info
    val headerRow = android.widget.LinearLayout(context).apply {
        orientation = android.widget.LinearLayout.HORIZONTAL
        gravity = android.view.Gravity.CENTER_VERTICAL
        layoutParams = android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    // "Ad" badge
    val adBadge = TextView(context).apply {
        text = "Ad • प्रायोजित"
        textSize = 10.5f
        setTextColor(android.graphics.Color.parseColor("#B45309"))
        setBackgroundColor(android.graphics.Color.parseColor("#FEF3C7"))
        setPadding(dpToPx(context, 6), dpToPx(context, 2), dpToPx(context, 6), dpToPx(context, 2))
        layoutParams = android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }
    headerRow.addView(adBadge)

    // Advertiser
    val advertiserView = TextView(context).apply {
        textSize = 11f
        setTextColor(android.graphics.Color.parseColor("#64748B"))
        setPadding(dpToPx(context, 8), 0, 0, 0)
        layoutParams = android.widget.LinearLayout.LayoutParams(
            0,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
    }
    headerRow.addView(advertiserView)
    nativeAdView.advertiserView = advertiserView
    container.addView(headerRow)

    // Content Row: Icon + Headline & Body
    val contentRow = android.widget.LinearLayout(context).apply {
        orientation = android.widget.LinearLayout.HORIZONTAL
        gravity = android.view.Gravity.CENTER_VERTICAL
        setPadding(0, dpToPx(context, 8), 0, 0)
        layoutParams = android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    // App/Brand Icon
    val iconView = ImageView(context).apply {
        layoutParams = android.widget.LinearLayout.LayoutParams(
            dpToPx(context, 42),
            dpToPx(context, 42)
        )
    }
    contentRow.addView(iconView)
    nativeAdView.iconView = iconView

    // Text column
    val textColumn = android.widget.LinearLayout(context).apply {
        orientation = android.widget.LinearLayout.VERTICAL
        setPadding(dpToPx(context, 10), 0, 0, 0)
        layoutParams = android.widget.LinearLayout.LayoutParams(
            0,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
    }

    val headlineView = TextView(context).apply {
        textSize = 14f
        setTypeface(null, android.graphics.Typeface.BOLD)
        setTextColor(android.graphics.Color.parseColor("#0F172A"))
        maxLines = 1
        ellipsize = android.text.TextUtils.TruncateAt.END
    }
    textColumn.addView(headlineView)
    nativeAdView.headlineView = headlineView

    val bodyView = TextView(context).apply {
        textSize = 12f
        setTextColor(android.graphics.Color.parseColor("#475569"))
        maxLines = 2
        ellipsize = android.text.TextUtils.TruncateAt.END
        setPadding(0, dpToPx(context, 2), 0, 0)
    }
    textColumn.addView(bodyView)
    nativeAdView.bodyView = bodyView
    contentRow.addView(textColumn)
    container.addView(contentRow)

    // Call to Action Button
    val ctaButton = Button(context).apply {
        textSize = 12.5f
        setTextColor(android.graphics.Color.WHITE)
        setBackgroundColor(android.graphics.Color.parseColor("#1E3A8A"))
        val params = android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 38)
        ).apply {
            setMargins(0, dpToPx(context, 10), 0, 0)
        }
        layoutParams = params
    }
    container.addView(ctaButton)
    nativeAdView.callToActionView = ctaButton

    nativeAdView.addView(container)
    populateNativeAdView(nativeAd, nativeAdView)
    return nativeAdView
}

private fun populateNativeAdView(nativeAd: NativeAd, nativeAdView: NativeAdView) {
    (nativeAdView.headlineView as? TextView)?.text = nativeAd.headline

    if (nativeAd.body == null) {
        nativeAdView.bodyView?.visibility = View.GONE
    } else {
        nativeAdView.bodyView?.visibility = View.VISIBLE
        (nativeAdView.bodyView as? TextView)?.text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        nativeAdView.callToActionView?.visibility = View.GONE
    } else {
        nativeAdView.callToActionView?.visibility = View.VISIBLE
        (nativeAdView.callToActionView as? Button)?.text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        nativeAdView.iconView?.visibility = View.GONE
    } else {
        nativeAdView.iconView?.visibility = View.VISIBLE
        (nativeAdView.iconView as? ImageView)?.setImageDrawable(nativeAd.icon?.drawable)
    }

    if (nativeAd.advertiser == null) {
        nativeAdView.advertiserView?.visibility = View.GONE
    } else {
        nativeAdView.advertiserView?.visibility = View.VISIBLE
        (nativeAdView.advertiserView as? TextView)?.text = nativeAd.advertiser
    }

    nativeAdView.setNativeAd(nativeAd)
}

private fun dpToPx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).toInt()
}
