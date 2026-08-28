package com.example.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.ads.AppOpenAdManager
import com.example.pdf.PdfDocumentManager
import com.example.ui.theme.CgBackground
import com.example.ui.theme.CgBluePrimary
import com.example.ui.theme.CgBluePrimaryDark
import com.example.ui.theme.CgBorder
import com.example.ui.theme.CgGoldAccent
import com.example.ui.theme.CgTextMuted
import com.example.ui.theme.CgTextPrimary
import com.example.ui.theme.CgTextSecondary
import com.example.ui.theme.CgYellowBadge

sealed interface PdfViewerState {
    data class Loading(val progress: Int, val message: String) : PdfViewerState
    data class Success(val pageCount: Int) : PdfViewerState
    data class Error(val message: String) : PdfViewerState
}

@Composable
fun PdfViewerScreen(
    chapterNumber: String,
    chapterTitle: String,
    pdfUrl: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    subjectTitle: String = "छत्तीसगढ़ सामान्य ज्ञान"
) {
    val context = LocalContext.current
    val pdfManager = remember { PdfDocumentManager(context) }
    var viewerState by remember { mutableStateOf<PdfViewerState>(PdfViewerState.Loading(0, "PDF लोड हो रहा है...")) }
    var refreshKey by remember { mutableIntStateOf(0) }

    // Clean up when leaving screen and protect PDF reading from App Open ads
    DisposableEffect(Unit) {
        AppOpenAdManager.isReadingPdf = true
        onDispose {
            AppOpenAdManager.isReadingPdf = false
            pdfManager.close()
        }
    }

    // Load PDF when URL or refreshKey changes
    LaunchedEffect(pdfUrl, refreshKey) {
        if (pdfUrl.isBlank()) {
            viewerState = PdfViewerState.Error("इस अध्याय का PDF लिंक उपलब्ध नहीं है।")
            return@LaunchedEffect
        }

        viewerState = PdfViewerState.Loading(5, "PDF डाउनलोड हो रहा है...")
        val result = pdfManager.loadPdf(
            url = pdfUrl,
            cacheKey = "${chapterNumber}_${chapterTitle.hashCode()}",
            forceRefresh = refreshKey > 0,
            onProgress = { progress ->
                viewerState = PdfViewerState.Loading(
                    progress = progress,
                    message = if (progress >= 95) "पेज तैयार किए जा रहे हैं..." else "डाउनलोडिंग ($progress%)..."
                )
            }
        )

        result.fold(
            onSuccess = { pageCount ->
                viewerState = PdfViewerState.Success(pageCount)
            },
            onFailure = { error ->
                viewerState = PdfViewerState.Error(
                    error.localizedMessage ?: "PDF लोड करने में असमर्थ। कृपया इंटरनेट कनेक्शन जांचें।"
                )
            }
        )
    }

    Scaffold(
        topBar = {
            PdfTopAppBar(
                chapterNumber = chapterNumber,
                chapterTitle = chapterTitle,
                subjectTitle = subjectTitle,
                pageCount = (viewerState as? PdfViewerState.Success)?.pageCount ?: 0,
                onBackClick = onBackClick,
                onRefreshClick = {
                    refreshKey++
                },
                onOpenExternalClick = {
                    val localFile = pdfManager.getLocalFile("${chapterNumber}_${chapterTitle.hashCode()}")
                    if (localFile != null) {
                        try {
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                localFile
                            )
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "PDF खोलें"))
                        } catch (e: Exception) {
                            // Fallback to web browser URL
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
                            context.startActivity(webIntent)
                        }
                    } else {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
                        context.startActivity(webIntent)
                    }
                }
            )
        },
        containerColor = CgBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = viewerState) {
                is PdfViewerState.Loading -> {
                    PdfLoadingView(
                        progress = state.progress,
                        message = state.message
                    )
                }
                is PdfViewerState.Error -> {
                    PdfErrorView(
                        errorMessage = state.message,
                        onRetry = { refreshKey++ },
                        onOpenInBrowser = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "ब्राउज़र खोलने में असमर्थ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                is PdfViewerState.Success -> {
                    PdfReaderView(
                        pageCount = state.pageCount,
                        pdfManager = pdfManager
                    )
                }
            }
        }
    }
}

@Composable
private fun PdfTopAppBar(
    chapterNumber: String,
    chapterTitle: String,
    pageCount: Int,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onOpenExternalClick: () -> Unit,
    modifier: Modifier = Modifier,
    subjectTitle: String = "छत्तीसगढ़ सामान्य ज्ञान"
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
                        .testTag("pdf_back_button")
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Chapters",
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
                        text = "अध्याय $chapterNumber: $chapterTitle",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (pageCount > 0) "कुल $pageCount पृष्ठ • $subjectTitle" else "$subjectTitle नोट्स",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1
                    )
                }

                // Refresh button
                IconButton(
                    onClick = onRefreshClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reload PDF",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Open in External App
                IconButton(
                    onClick = onOpenExternalClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Open in External App",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
private fun PdfReaderView(
    pageCount: Int,
    pdfManager: PdfDocumentManager,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Track currently visible page
    val currentVisiblePage by remember {
        derivedStateOf {
            (listState.firstVisibleItemIndex + 1).coerceIn(1, pageCount)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE2E8F0)) // Clean neutral reading backdrop
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(1f, 3.5f)
                    scale = newScale
                    if (newScale > 1f) {
                        offset += pan
                    } else {
                        offset = Offset.Zero
                    }
                }
            }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .testTag("pdf_pages_list"),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 16.dp,
                bottom = 48.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = pageCount,
                key = { it }
            ) { pageIndex ->
                PdfPageItem(
                    pageIndex = pageIndex,
                    pageCount = pageCount,
                    pdfManager = pdfManager
                )
            }
        }

        // Floating Page Indicator Pill
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xDD1E293B),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "पृष्ठ $currentVisiblePage / $pageCount",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )

                    if (scale > 1.05f) {
                        Text(
                            text = "• ${(scale * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CgYellowBadge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Floating Zoom Controls Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 4.dp,
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(CgBorder)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(2.dp)
                ) {
                    // Zoom Out
                    IconButton(
                        onClick = {
                            scale = (scale - 0.25f).coerceAtLeast(1f)
                            if (scale == 1f) offset = Offset.Zero
                        },
                        modifier = Modifier.size(36.dp),
                        enabled = scale > 1f
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Zoom Out",
                            tint = if (scale > 1f) CgTextPrimary else CgTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Reset Zoom
                    AnimatedVisibility(visible = scale > 1.05f) {
                        IconButton(
                            onClick = {
                                scale = 1f
                                offset = Offset.Zero
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset Zoom",
                                tint = CgBluePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Zoom In
                    IconButton(
                        onClick = {
                            scale = (scale + 0.25f).coerceAtMost(3.5f)
                        },
                        modifier = Modifier.size(36.dp),
                        enabled = scale < 3.5f
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Zoom In",
                            tint = if (scale < 3.5f) CgTextPrimary else CgTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PdfPageItem(
    pageIndex: Int,
    pageCount: Int,
    pdfManager: PdfDocumentManager,
    modifier: Modifier = Modifier
) {
    var pageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(pageIndex) {
        isLoading = true
        val bitmap = pdfManager.renderPage(pageIndex)
        pageBitmap = bitmap
        isLoading = false
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("pdf_page_card_$pageIndex"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(CgBorder)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bitmap = pageBitmap
            if (bitmap != null) {
                // Display normalized page with consistent width across all pages
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Page ${pageIndex + 1} of $pageCount",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
            } else {
                // Placeholder loading container with standard A4 aspect ratio
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.707f)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = CgBluePrimary,
                            strokeWidth = 2.5.dp
                        )
                        Text(
                            text = "पृष्ठ ${pageIndex + 1} लोड हो रहा है...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CgTextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // Subtle page footer line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC))
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "- ${pageIndex + 1} -",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CgTextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
private fun PdfLoadingView(
    progress: Int,
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(CgBorder)
            ),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = CgBluePrimary,
                    strokeWidth = 4.dp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "नोट्स लोड हो रहे हैं",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CgTextPrimary,
                            fontSize = 16.sp
                        )
                    )

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CgTextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                if (progress in 1..99) {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = CgBluePrimary,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }
        }
    }
}

@Composable
private fun PdfErrorView(
    errorMessage: String,
    onRetry: () -> Unit,
    onOpenInBrowser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(CgBorder)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "PDF खोलने में समस्या",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CgTextPrimary,
                            fontSize = 16.sp
                        )
                    )

                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CgTextSecondary,
                            fontSize = 12.5.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onOpenInBrowser,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ब्राउज़र में", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onRetry,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CgBluePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("पुनः प्रयास", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
