package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CgBackground
import com.example.ui.theme.CgBluePrimary
import com.example.ui.theme.CgBluePrimaryContainer
import com.example.ui.theme.CgBluePrimaryDark
import com.example.ui.theme.CgBorder
import com.example.ui.theme.CgGoldAccent
import com.example.ui.theme.CgSurface
import com.example.ui.theme.CgTextMuted
import com.example.ui.theme.CgTextPrimary
import com.example.ui.theme.CgTextSecondary
import com.example.ui.theme.CgYellowBadge

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            PrivacyPolicyTopAppBar(onBackClick = onBackClick)
        },
        containerColor = CgBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("privacy_policy_scroll_container"),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Overview & Educational Purpose Badge Card
            item(key = "privacy_hero_card") {
                PrivacyHeroCard()
            }

            // 2. Core Privacy Commitments (No Data Collection & Zero Misuse)
            item(key = "privacy_commitments_card") {
                PrivacyCommitmentsCard()
            }

            // 3. Internet & Google Drive PDFs Card
            item(key = "privacy_drive_card") {
                InternetAndDriveCard()
            }

            // 4. Content Ownership & Removal Request Card
            item(key = "privacy_removal_card") {
                ContentOwnershipCard()
            }

            // 5. Contact Us Card (Clickable Email)
            item(key = "privacy_contact_card") {
                ContactUsCard(
                    onSendEmail = { email -> openEmailClient(context, email) }
                )
            }

            // 6. Footer
            item(key = "privacy_footer_card") {
                PrivacyFooterCard()
            }
        }
    }
}

@Composable
private fun PrivacyPolicyTopAppBar(
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
                        .testTag("privacy_back_button")
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
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 19.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "गोपनीयता नीति एवं डेटा सुरक्षा",
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
                        text = "Safe & Private",
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
private fun PrivacyHeroCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("privacy_hero_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFECFDF5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PrivacyTip,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CgTextPrimary,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "Last updated: 2026",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CgTextMuted,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Chhattisgarh GS app is developed for educational purposes only.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CgTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.5.sp,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "All content provided in this app is for educational purposes only.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CgTextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                )
            )
        }
    }
}

@Composable
private fun PrivacyCommitmentsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("privacy_commitments_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.Shield,
                iconTint = CgBluePrimary,
                iconBg = CgBluePrimaryContainer,
                title = "Data Privacy & Protection"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // No Personal Data Collection Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEFF6FF))
                    .border(BorderStroke(1.dp, Color(0xFFBFDBFE)), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = CgBluePrimary,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "No Personal Information Collection",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CgBluePrimaryDark,
                                fontSize = 13.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "We do not collect any personal information from users.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CgTextPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // No Data Stored/Shared/Misused Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(BorderStroke(1.dp, CgBorder), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Zero Data Storage & Sharing",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46),
                                fontSize = 13.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "We do not store, share, or misuse any user data.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CgTextPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InternetAndDriveCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("privacy_drive_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.CloudDownload,
                iconTint = Color(0xFFD97706),
                iconBg = Color(0xFFFEF3C7),
                title = "Internet Usage & PDF Materials"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "This app uses an internet connection to load study materials (PDF files) from Google Drive.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CgTextPrimary,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The connection is utilized strictly for fetching educational document streams directly to render notes inside the integrated PDF viewer.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CgTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

@Composable
private fun ContentOwnershipCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("privacy_content_ownership_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFFDE68A))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.DeleteOutline,
                iconTint = Color(0xFFB45309),
                iconBg = Color(0xFFFEF3C7),
                title = "Content Ownership & Removal"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "If any content belongs to you and you want it removed, please contact us.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF92400E),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We respect intellectual property rights and will promptly review and take appropriate action upon receiving valid requests.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF78350F),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

@Composable
private fun ContactUsCard(
    onSendEmail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("privacy_contact_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CgBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionHeader(
                icon = Icons.Default.Email,
                iconTint = CgBluePrimary,
                iconBg = CgBluePrimaryContainer,
                title = "Contact Us"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "For any queries, contact us at:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CgTextSecondary,
                    fontSize = 13.5.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSendEmail("vijayekka804@gmail.com") }
                    .testTag("privacy_contact_email_button"),
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
                                fontSize = 14.sp
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
private fun PrivacyFooterCard() {
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
            text = "Committed to Learner Privacy & Security",
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

private fun openEmailClient(context: Context, email: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "CG GS App - Privacy Query / Content Removal")
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
