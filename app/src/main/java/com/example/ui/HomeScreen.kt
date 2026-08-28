package com.example.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.BannerAdView
import com.example.ads.BannerType
import com.example.model.StudyCategory
import com.example.model.StudyDataProvider
import com.example.ui.components.AppDrawer
import com.example.ui.components.CategoryCard
import com.example.ui.components.CgTopAppBar
import com.example.ui.components.HeroBannerCard
import com.example.ui.components.QuickAccessSection
import com.example.ui.theme.CgBackground
import com.example.ui.theme.CgBluePrimary
import com.example.ui.theme.CgBluePrimaryDark
import com.example.ui.theme.CgGoldAccent
import com.example.ui.theme.CgTextMuted
import com.example.ui.theme.CgTextPrimary
import com.example.ui.theme.CgTextSecondary
import com.example.ui.theme.CgYellowBadge
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigateToHistory: () -> Unit = {},
    onNavigateToGeography: () -> Unit = {},
    onNavigateToCulture: () -> Unit = {},
    onNavigateToPolity: () -> Unit = {},
    onNavigateToSchemes: () -> Unit = {},
    onNavigateToAnyaJankari: () -> Unit = {},
    onNavigateToAboutUs: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToContactUs: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Dialog state for selected category preview
    var selectedCategory by remember { mutableStateOf<StudyCategory?>(null) }
    var selectedInfoDialog by remember { mutableStateOf<String?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onItemClick = { itemTitle ->
                    coroutineScope.launch {
                        drawerState.close()
                        when (itemTitle) {
                            "About", "About Us" -> onNavigateToAboutUs()
                            "Privacy Policy" -> onNavigateToPrivacyPolicy()
                            "Contact Us", "Contact" -> onNavigateToContactUs()
                            "Share This App", "Share App" -> shareApp(context)
                            "Rate App" -> rateApp(context)
                            else -> selectedInfoDialog = itemTitle
                        }
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                CgTopAppBar(
                    onMenuClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                    onNotificationsClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("CG GS अध्ययन सामग्री एवं नोट्स नियमित रूप से अपडेट किए जाते हैं")
                        }
                    }
                )
            },
            bottomBar = {
                BannerAdView(
                    bannerType = BannerType.BANNER_1,
                    modifier = Modifier.navigationBarsPadding()
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = CgBackground
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("home_scroll_container"),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 1. Hero Banner Section
                item(key = "hero_banner") {
                    HeroBannerCard()
                }

                // 2. Main CG General Studies Categories Section Header
                item(key = "categories_header") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(CgBluePrimary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = "CG General Studies",
                                    tint = CgBluePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "CG General Studies",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CgTextPrimary,
                                        fontSize = 17.sp
                                    )
                                )
                                Text(
                                    text = "छत्तीसगढ़ सामान्य अध्ययन - मुख्य विषय",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        color = CgTextMuted
                                    )
                                )
                            }
                        }

                        // Category count chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(CgBluePrimary.copy(alpha = 0.08f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "6 विषय",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CgBluePrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // 3. 2-Column Responsive Grid for Study Categories
                val categories = StudyDataProvider.mainCategories
                val rows = categories.chunked(2)
                items(rows.size, key = { "cat_row_$it" }) { rowIndex ->
                    val pair = rows[rowIndex]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CategoryCard(
                            category = pair[0],
                            onClick = {
                                when (pair[0].id) {
                                    "history" -> onNavigateToHistory()
                                    "geography" -> onNavigateToGeography()
                                    "culture" -> onNavigateToCulture()
                                    "polity" -> onNavigateToPolity()
                                    "schemes" -> onNavigateToSchemes()
                                    "misc" -> onNavigateToAnyaJankari()
                                    else -> selectedCategory = pair[0]
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        if (pair.size > 1) {
                            CategoryCard(
                                category = pair[1],
                                onClick = {
                                    when (pair[1].id) {
                                        "history" -> onNavigateToHistory()
                                        "geography" -> onNavigateToGeography()
                                        "culture" -> onNavigateToCulture()
                                        "polity" -> onNavigateToPolity()
                                        "schemes" -> onNavigateToSchemes()
                                        "misc" -> onNavigateToAnyaJankari()
                                        else -> selectedCategory = pair[1]
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                // 4. Quick Access Section (Notes, Key Topics, PYQs, Strategy)
                item(key = "quick_access") {
                    QuickAccessSection(
                        items = StudyDataProvider.quickAccessList,
                        onItemClick = { item ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("${item.titleHindi} - शीघ्र उपलब्ध होगा")
                            }
                        }
                    )
                }

                // 5. Bottom Educational Motivational Banner
                item(key = "bottom_banner") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(CgBluePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Success",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "CG GS • सम्पूर्ण परीक्षा तैयारी",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CgTextPrimary,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "CGPSC एवं CG व्यापम परीक्षाओं के लिए प्रामाणिक अध्ययन सामग्री",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = CgTextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Category Topic Preview Dialog (Extensible for upcoming chapters/books)
    selectedCategory?.let { category ->
        AlertDialog(
            onDismissRequest = { selectedCategory = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(category.iconBgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.titleHindi,
                            tint = category.iconTintColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = category.titleHindi,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CgTextPrimary
                            )
                        )
                        Text(
                            text = category.titleEnglish,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CgTextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "प्रमुख अध्याय एवं विषय सूची (${category.chapterCount}):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CgBluePrimary
                        )
                    )
                    category.topics.forEach { topic ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = category.iconTintColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = topic,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = CgTextPrimary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedCategory = null },
                    colors = ButtonDefaults.buttonColors(containerColor = CgBluePrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("ठीक है (Close)")
                }
            }
        )
    }

    // Info Dialog for Menu items
    selectedInfoDialog?.let { title ->
        AlertDialog(
            onDismissRequest = { selectedInfoDialog = null },
            shape = RoundedCornerShape(18.dp),
            containerColor = Color.White,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CgTextPrimary
                    )
                )
            },
            text = {
                Text(
                    text = when (title) {
                        "About" -> "CG GS (Chhattisgarh General Studies) छत्तीसगढ़ राज्य की सभी प्रतियोगी परीक्षाओं (CGPSC, CG व्यापम) के लिए एक समर्पित एवं निःशुल्क अध्ययन ऐप है।\n\nDeveloper: Vijay Kumar Ekka\nEmail: vijayekka804@gmail.com\nVersion: 2026.1"
                        "Privacy Policy" -> "CG GS उपयोगकर्ता की गोपनीयता का पूर्ण सम्मान करता है। यह ऐप व्यक्तिगत डेटा एकत्र नहीं करता है।"
                        "Contact Us" -> "किसी भी प्रश्न या सुझाव हेतु संपर्क करें:\n\nDeveloper: Vijay Kumar Ekka\nEmail: vijayekka804@gmail.com"
                        "Share This App" -> "अपने मित्रों एवं साथी परीक्षार्थियों के साथ CG GS ऐप साझा करें।"
                        "Rate App" -> "Google Play Store पर 5 स्टार रेटिंग देकर हमारा उत्साहवर्धन करें!"
                        else -> "यह सुविधा शीघ्र ही उपलब्ध होगी।"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CgTextSecondary,
                        lineHeight = 20.sp
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { selectedInfoDialog = null }) {
                    Text("OK", color = CgBluePrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/**
 * Opens the Android system share sheet with the CG GS app download message and Play Store link.
 */
private fun shareApp(context: Context) {
    val shareMessage = "Download Chhattisgarh GS App for CGPSC & CG Vyapam preparation:\nhttps://play.google.com/store/apps/details?id=com.cg.gs.app&hl=en"
    try {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }
        val chooserIntent = Intent.createChooser(sendIntent, "Share Chhattisgarh GS App via").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooserIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No sharing application found", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to share app at this moment", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Attempts to open Google Play Store app directly to rate and review the app.
 * Gracefully falls back to browser if Play Store is not installed/available.
 */
private fun rateApp(context: Context) {
    val playStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=com.cg.gs.app&showAllReviews=true")
    val marketUri = Uri.parse("market://details?id=com.cg.gs.app&showAllReviews=true")

    try {
        // Try opening with Play Store market URI & package
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri).apply {
            setPackage("com.android.vending")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(marketIntent)
    } catch (e: ActivityNotFoundException) {
        // Fallback 1: Try standard web URL with market/browser chooser or web browser
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, playStoreUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        } catch (e2: Exception) {
            Toast.makeText(context, "Unable to open Play Store", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        // Fallback 2: General fallback to web browser
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, playStoreUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        } catch (e2: Exception) {
            Toast.makeText(context, "Unable to open Play Store", Toast.LENGTH_SHORT).show()
        }
    }
}
