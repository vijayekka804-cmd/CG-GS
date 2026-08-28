package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.BannerAdView
import com.example.ads.BannerType
import com.example.ads.NativeAdCard
import com.example.model.HistoryChapter
import com.example.model.StudyDataProvider
import com.example.ui.theme.CgBackground
import com.example.ui.theme.CgBluePrimary
import com.example.ui.theme.CgBluePrimaryDark
import com.example.ui.theme.CgBorder
import com.example.ui.theme.CgGoldAccent
import com.example.ui.theme.CgTextMuted
import com.example.ui.theme.CgTextPrimary
import com.example.ui.theme.CgTextSecondary
import com.example.ui.theme.CgYellowBadge
import kotlinx.coroutines.launch

@Composable
fun HistoryChapterListScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onChapterClick: (HistoryChapter) -> Unit = {}
) {
    val chapters = StudyDataProvider.historyChapters
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HistoryTopAppBar(
                title = "छत्तीसगढ़ इतिहास",
                chapterCount = "${chapters.size} अध्याय",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BannerAdView(
                bannerType = BannerType.BANNER_2,
                modifier = Modifier.navigationBarsPadding()
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = CgBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("history_chapters_list"),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header summary info card
            item(key = "header_info_card") {
                HistoryHeaderCard(totalChapters = chapters.size)
            }

            // Chapter List items with Native Ad inserted naturally
            val firstBatch = chapters.take(3)
            val remainingBatch = chapters.drop(3)

            items(
                items = firstBatch,
                key = { it.id }
            ) { chapter ->
                ChapterItemCard(
                    chapter = chapter,
                    onClick = {
                        if (chapter.driveUrl.isNotEmpty()) {
                            onChapterClick(chapter)
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "अध्याय ${chapter.chapterNumber}: ${chapter.title} (PDF लिंक शीघ्र जोड़ा जाएगा)"
                                )
                            }
                            onChapterClick(chapter)
                        }
                    }
                )
            }

            // Native Ad Card
            if (chapters.size >= 3) {
                item(key = "history_native_ad") {
                    NativeAdCard(modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            items(
                items = remainingBatch,
                key = { it.id }
            ) { chapter ->
                ChapterItemCard(
                    chapter = chapter,
                    onClick = {
                        if (chapter.driveUrl.isNotEmpty()) {
                            onChapterClick(chapter)
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    "अध्याय ${chapter.chapterNumber}: ${chapter.title} (PDF लिंक शीघ्र जोड़ा जाएगा)"
                                )
                            }
                            onChapterClick(chapter)
                        }
                    }
                )
            }

            // Bottom informational pill
            item(key = "bottom_note") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CG GS • CGPSC & CG Vyapam Exam Preparation",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CgTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryTopAppBar(
    title: String,
    chapterCount: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CgBluePrimaryDark,
                            CgBluePrimary
                        )
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .testTag("back_button")
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Title and Subtitle
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 19.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "Chhattisgarh History • संपूर्ण अध्यायवार नोट्स",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1
                    )
                }

                // Chapter count pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CgGoldAccent.copy(alpha = 0.25f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = chapterCount,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CgYellowBadge,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun HistoryHeaderCard(
    totalChapters: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(CgBorder)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFFBEB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HistoryEdu,
                    contentDescription = "History",
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "छत्तीसगढ़ इतिहास विषय सूची",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CgTextPrimary,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "प्राचीन काल से राज्य निर्माण आंदोलन तक ($totalChapters अध्याय)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CgTextSecondary,
                        fontSize = 11.5.sp
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFF6FF))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "CGPSC",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CgBluePrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun ChapterItemCard(
    chapter: HistoryChapter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("chapter_item_${chapter.id}")
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 3.dp
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(CgBorder)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chapter Number Badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CgBluePrimary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chapter.chapterNumber,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CgBluePrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Exact Chapter Title Text from Reference Image
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.5.sp,
                        color = CgTextPrimary,
                        lineHeight = 21.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action / Link Indicator
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Open Chapter ${chapter.chapterNumber}",
                    tint = CgTextMuted.copy(alpha = 0.6f),
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
