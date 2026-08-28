package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CgBackground
import com.example.ui.theme.CgBluePrimary
import com.example.ui.theme.CgBluePrimaryContainer
import com.example.ui.theme.CgBluePrimaryDark
import com.example.ui.theme.CgBorder
import com.example.ui.theme.CgGoldAccent
import com.example.ui.theme.CgSecondary
import com.example.ui.theme.CgSurface
import com.example.ui.theme.CgTextMuted
import com.example.ui.theme.CgTextPrimary
import com.example.ui.theme.CgTextSecondary
import com.example.ui.theme.CgYellowBadge

@Composable
fun AboutUsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AboutUsTopAppBar(onBackClick = onBackClick)
        },
        containerColor = CgBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("about_us_scroll_container"),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. App Header / Branding Card
            item(key = "app_branding_card") {
                AppBrandingCard()
            }

            // 2. About App Description Card
            item(key = "about_desc_card") {
                AboutDescriptionCard()
            }

            // 3. Features Section Card
            item(key = "features_card") {
                FeaturesSectionCard()
            }

            // 4. Disclaimer Card
            item(key = "disclaimer_card") {
                DisclaimerCard()
            }

            // 5. Official Sources Card (Clickable links)
            item(key = "official_sources_card") {
                OfficialSourcesCard(
                    onOpenUrl = { url -> openWebUrl(context, url) }
                )
            }

            // 6. Developer & Contact Card
            item(key = "developer_contact_card") {
                DeveloperAndContactCard(
                    onSendEmail = { email -> openEmailClient(context, email) }
                )
            }

            // 7. Footer / Version Card
            item(key = "footer_version_card") {
                FooterVersionCard()
            }
        }
    }
}

@Composable
private fun AboutUsTopAppBar(
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
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("about_us_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "वापस जाएं (Back)",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "About Us",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 19.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "हमारे बारे में एवं ऐप परिचय",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CgGoldAccent.copy(alpha = 0.25f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "CG GS App",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CgYellowBadge,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AppBrandingCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("about_branding_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 3.dp,
                modifier = Modifier.size(64.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cg_gs_logo),
                    contentDescription = "CG GS Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Chhattisgarh GS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CgTextPrimary,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = "Target CG • Exam Preparation",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CgBluePrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "CGPSC & CG Vyapam Preparation Portal",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CgTextMuted,
                        fontSize = 11.5.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun AboutDescriptionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("about_description_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.Info,
                iconTint = CgBluePrimary,
                iconBg = CgBluePrimaryContainer,
                title = "About the App"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Chhattisgarh GS ek educational app hai jo CGPSC, CG Vyapam aur anya Chhattisgarh competitive exams ki tayari ke liye banayi gayi hai.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CgTextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Is app me aapko chapter-wise notes aur PDF materials milte hain, jo aapki tayari ko easy aur systematic banate hain.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CgTextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Yeh app sirf educational purpose ke liye banayi gayi hai.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CgBluePrimaryDark,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun FeaturesSectionCard() {
    val features = listOf(
        "Chhattisgarh GS ke sabhi important topics cover kiye gaye hain",
        "Chapter-wise organized notes",
        "PDF format me study material",
        "Easy navigation aur simple UI"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("about_features_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.School,
                iconTint = Color(0xFF059669),
                iconBg = Color(0xFFECFDF5),
                title = "Features"
            )

            Spacer(modifier = Modifier.height(12.dp))

            features.forEach { feature ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CgTextPrimary,
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DisclaimerCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("about_disclaimer_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFFDE68A))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.WarningAmber,
                iconTint = Color(0xFFD97706),
                iconBg = Color(0xFFFEF3C7),
                title = "Disclaimer"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Exact Hindi Disclaimer Quote
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Text(
                    text = "“यदि आप इस एप्लिकेशन से परीक्षा की तैयारी कर रहे हैं, तो साथ में अपडेटेड जानकारी भी आधिकारिक स्रोत से अवश्य जांचते रहें, क्योंकि कुछ जानकारियाँ समय-समय पर बदलती रहती हैं। जानकारी में परिवर्तन होने की स्थिति में यह ऐप उसकी कोई जिम्मेदारी नहीं लेती है।”",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF92400E),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        lineHeight = 21.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color(0xFFB45309),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "This app is not affiliated with any government organization.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF78350F),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Information is provided for educational purposes only.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF78350F),
                    fontSize = 11.5.sp
                )
            )
        }
    }
}

@Composable
private fun OfficialSourcesCard(
    onOpenUrl: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("about_official_sources_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.Language,
                iconTint = CgSecondary,
                iconBg = Color(0xFFE0F2FE),
                title = "Official Sources"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "For official notifications, syllabi, exam dates, and authentic updates, refer to the official government portals:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CgTextMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // CGPSC Link Item
            OfficialSourceItem(
                portalName = "CGPSC",
                portalDescription = "Chhattisgarh Public Service Commission",
                url = "https://psc.cg.gov.in/",
                onClick = { onOpenUrl("https://psc.cg.gov.in/") },
                testTag = "link_cgpsc"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // CG Vyapam Link Item
            OfficialSourceItem(
                portalName = "CG Vyapam",
                portalDescription = "Chhattisgarh Professional Examination Board",
                url = "https://vyapamcg.cgstate.gov.in/",
                onClick = { onOpenUrl("https://vyapamcg.cgstate.gov.in/") },
                testTag = "link_cg_vyapam"
            )
        }
    }
}

@Composable
private fun OfficialSourceItem(
    portalName: String,
    portalDescription: String,
    url: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CgBluePrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = CgBluePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = portalName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CgTextPrimary,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = portalDescription,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CgTextMuted,
                        fontSize = 11.sp
                    )
                )
                Text(
                    text = url,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CgBluePrimary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = "Open Link",
                tint = CgBluePrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun DeveloperAndContactCard(
    onSendEmail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("about_dev_contact_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Developer Sub-section
            SectionHeader(
                icon = Icons.Default.Person,
                iconTint = Color(0xFF7C3AED),
                iconBg = Color(0xFFFAF5FF),
                title = "Developer"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFAF5FF))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name:",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CgTextMuted,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Vijay Kumar Ekka",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CgTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CgBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // Contact Sub-section
            SectionHeader(
                icon = Icons.Default.Email,
                iconTint = CgBluePrimary,
                iconBg = CgBluePrimaryContainer,
                title = "Contact"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSendEmail("vijayekka804@gmail.com") }
                    .testTag("contact_email_button"),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CgBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CgTextMuted,
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = "vijayekka804@gmail.com",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CgBluePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Send Email",
                        tint = CgBluePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FooterVersionCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CG GS • CHHATTISGARH GENERAL STUDIES",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = CgTextMuted,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
            )
        )
        Text(
            text = "Version 2026.1 • Made for Aspirants",
            style = MaterialTheme.typography.bodySmall.copy(
                color = CgTextMuted.copy(alpha = 0.7f),
                fontSize = 10.5.sp
            )
        )
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = CgTextPrimary,
                fontSize = 16.sp
            )
        )
    }
}

private fun openWebUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "URL नहीं खोला जा सका: $url", Toast.LENGTH_SHORT).show()
    }
}

private fun openEmailClient(context: Context, email: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "CG GS App - Feedback / Query")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$email")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        } catch (e2: Exception) {
            Toast.makeText(context, "ईमेल ऐप नहीं मिला: $email", Toast.LENGTH_SHORT).show()
        }
    }
}
