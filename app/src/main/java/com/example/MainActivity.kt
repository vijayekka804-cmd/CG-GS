package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ads.AdManager
import com.example.ads.AppOpenAdManager
import com.example.ui.CultureChapterListScreen
import com.example.ui.GeographyChapterListScreen
import com.example.ui.HistoryChapterListScreen
import com.example.ui.HomeScreen
import com.example.ui.PolityChapterListScreen
import com.example.ui.SchemeChapterListScreen
import com.example.ui.AnyaJankariChapterListScreen
import com.example.ui.PdfViewerScreen
import com.example.ui.AboutUsScreen
import com.example.ui.PrivacyPolicyScreen
import com.example.ui.ContactUsScreen
import com.example.ui.theme.MyApplicationTheme

enum class AppScreen {
  HOME,
  HISTORY_CHAPTERS,
  GEOGRAPHY_CHAPTERS,
  CULTURE_CHAPTERS,
  POLITY_CHAPTERS,
  SCHEMES_CHAPTERS,
  ANYA_JANKARI_CHAPTERS,
  PDF_VIEWER,
  ABOUT_US,
  PRIVACY_POLICY,
  CONTACT_US
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Initialize Google Mobile Ads SDK and managers safely in background
    AdManager.initialize(this)
    AppOpenAdManager.init(application)

    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          CgGsAppNavigation()
        }
      }
    }
  }
}

@Composable
fun CgGsAppNavigation() {
  val context = LocalContext.current
  val activity = context as? Activity

  var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
  var previousScreen by remember { mutableStateOf(AppScreen.HOME) }
  var activePdfChapterNumber by remember { mutableStateOf("01") }
  var activePdfChapterTitle by remember { mutableStateOf("") }
  var activePdfUrl by remember { mutableStateOf("") }
  var activePdfSubjectTitle by remember { mutableStateOf("छत्तीसगढ़ इतिहास") }

  BackHandler(enabled = currentScreen != AppScreen.HOME) {
    if (currentScreen == AppScreen.PDF_VIEWER) {
      currentScreen = previousScreen
      activity?.let { AdManager.onUserTransition(it) }
    } else {
      currentScreen = AppScreen.HOME
    }
  }

  AnimatedContent(
    targetState = currentScreen,
    transitionSpec = { fadeIn() togetherWith fadeOut() },
    label = "screen_transition"
  ) { screen ->
    when (screen) {
      AppScreen.HOME -> {
        HomeScreen(
          onNavigateToHistory = {
            currentScreen = AppScreen.HISTORY_CHAPTERS
          },
          onNavigateToGeography = {
            currentScreen = AppScreen.GEOGRAPHY_CHAPTERS
          },
          onNavigateToCulture = {
            currentScreen = AppScreen.CULTURE_CHAPTERS
          },
          onNavigateToPolity = {
            currentScreen = AppScreen.POLITY_CHAPTERS
          },
          onNavigateToSchemes = {
            currentScreen = AppScreen.SCHEMES_CHAPTERS
          },
          onNavigateToAnyaJankari = {
            currentScreen = AppScreen.ANYA_JANKARI_CHAPTERS
          },
          onNavigateToAboutUs = {
            currentScreen = AppScreen.ABOUT_US
          },
          onNavigateToPrivacyPolicy = {
            currentScreen = AppScreen.PRIVACY_POLICY
          },
          onNavigateToContactUs = {
            currentScreen = AppScreen.CONTACT_US
          }
        )
      }
      AppScreen.HISTORY_CHAPTERS -> {
        HistoryChapterListScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          },
          onChapterClick = { chapter ->
            if (chapter.driveUrl.isNotEmpty()) {
              activePdfChapterNumber = chapter.chapterNumber
              activePdfChapterTitle = chapter.title
              activePdfUrl = chapter.driveUrl
              activePdfSubjectTitle = "छत्तीसगढ़ इतिहास"
              previousScreen = AppScreen.HISTORY_CHAPTERS
              currentScreen = AppScreen.PDF_VIEWER
            }
          }
        )
      }
      AppScreen.GEOGRAPHY_CHAPTERS -> {
        GeographyChapterListScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          },
          onChapterClick = { chapter ->
            if (chapter.driveUrl.isNotEmpty()) {
              activePdfChapterNumber = chapter.chapterNumber
              activePdfChapterTitle = chapter.title
              activePdfUrl = chapter.driveUrl
              activePdfSubjectTitle = "छत्तीसगढ़ भूगोल"
              previousScreen = AppScreen.GEOGRAPHY_CHAPTERS
              currentScreen = AppScreen.PDF_VIEWER
            }
          }
        )
      }
      AppScreen.CULTURE_CHAPTERS -> {
        CultureChapterListScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          },
          onChapterClick = { chapter ->
            if (chapter.driveUrl.isNotEmpty()) {
              activePdfChapterNumber = chapter.chapterNumber
              activePdfChapterTitle = chapter.title
              activePdfUrl = chapter.driveUrl
              activePdfSubjectTitle = "कला एवं संस्कृति"
              previousScreen = AppScreen.CULTURE_CHAPTERS
              currentScreen = AppScreen.PDF_VIEWER
            }
          }
        )
      }
      AppScreen.POLITY_CHAPTERS -> {
        PolityChapterListScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          },
          onChapterClick = { chapter ->
            if (chapter.driveUrl.isNotEmpty()) {
              activePdfChapterNumber = chapter.chapterNumber
              activePdfChapterTitle = chapter.title
              activePdfUrl = chapter.driveUrl
              activePdfSubjectTitle = "छत्तीसगढ़ राज्यव्यवस्था"
              previousScreen = AppScreen.POLITY_CHAPTERS
              currentScreen = AppScreen.PDF_VIEWER
            }
          }
        )
      }
      AppScreen.SCHEMES_CHAPTERS -> {
        SchemeChapterListScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          },
          onChapterClick = { chapter ->
            if (chapter.driveUrl.isNotEmpty()) {
              activePdfChapterNumber = chapter.chapterNumber
              activePdfChapterTitle = chapter.title
              activePdfUrl = chapter.driveUrl
              activePdfSubjectTitle = "योजना एवं प्रथम"
              previousScreen = AppScreen.SCHEMES_CHAPTERS
              currentScreen = AppScreen.PDF_VIEWER
            }
          }
        )
      }
      AppScreen.ANYA_JANKARI_CHAPTERS -> {
        AnyaJankariChapterListScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          },
          onChapterClick = { chapter ->
            if (chapter.driveUrl.isNotEmpty()) {
              activePdfChapterNumber = chapter.chapterNumber
              activePdfChapterTitle = chapter.title
              activePdfUrl = chapter.driveUrl
              activePdfSubjectTitle = "छत्तीसगढ़ अन्य जानकारी"
              previousScreen = AppScreen.ANYA_JANKARI_CHAPTERS
              currentScreen = AppScreen.PDF_VIEWER
            }
          }
        )
      }
      AppScreen.PDF_VIEWER -> {
        PdfViewerScreen(
          chapterNumber = activePdfChapterNumber,
          chapterTitle = activePdfChapterTitle,
          pdfUrl = activePdfUrl,
          subjectTitle = activePdfSubjectTitle,
          onBackClick = {
            currentScreen = previousScreen
            activity?.let { AdManager.onUserTransition(it) }
          }
        )
      }
      AppScreen.ABOUT_US -> {
        AboutUsScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          }
        )
      }
      AppScreen.PRIVACY_POLICY -> {
        PrivacyPolicyScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          }
        )
      }
      AppScreen.CONTACT_US -> {
        ContactUsScreen(
          onBackClick = {
            currentScreen = AppScreen.HOME
          }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ContactUsPreview() {
  MyApplicationTheme {
    ContactUsScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyPreview() {
  MyApplicationTheme {
    PrivacyPolicyScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun AboutUsPreview() {
  MyApplicationTheme {
    AboutUsScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  MyApplicationTheme {
    HomeScreen()
  }
}

@Preview(showBackground = true)
@Composable
fun HistoryChaptersPreview() {
  MyApplicationTheme {
    HistoryChapterListScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun CultureChaptersPreview() {
  MyApplicationTheme {
    CultureChapterListScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun PolityChaptersPreview() {
  MyApplicationTheme {
    PolityChapterListScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun SchemesChaptersPreview() {
  MyApplicationTheme {
    SchemeChapterListScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun AnyaJankariChaptersPreview() {
  MyApplicationTheme {
    AnyaJankariChapterListScreen(onBackClick = {})
  }
}

@Preview(showBackground = true)
@Composable
fun PdfViewerPreview() {
  MyApplicationTheme {
    PdfViewerScreen(
      chapterNumber = "01",
      chapterTitle = "परिचय व नामकरण",
      pdfUrl = "",
      onBackClick = {}
    )
  }
}


