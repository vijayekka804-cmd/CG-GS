package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.CgCatCultureBg
import com.example.ui.theme.CgCatCultureIcon
import com.example.ui.theme.CgCatGeographyBg
import com.example.ui.theme.CgCatGeographyIcon
import com.example.ui.theme.CgCatHistoryBg
import com.example.ui.theme.CgCatHistoryIcon
import com.example.ui.theme.CgCatMiscBg
import com.example.ui.theme.CgCatMiscIcon
import com.example.ui.theme.CgCatPolityBg
import com.example.ui.theme.CgCatPolityIcon
import com.example.ui.theme.CgCatSchemesBg
import com.example.ui.theme.CgCatSchemesIcon

/**
 * Represents the primary 6 study subject categories from the original CG GS application.
 */
data class StudyCategory(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val subtitle: String,
    val examTag: String = "CGPSC | CGVYAPAM",
    val chapterCount: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTintColor: Color,
    val topics: List<String> = emptyList()
)

/**
 * Represents quick access items for study material & revision.
 */
data class QuickAccessItem(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val badge: String,
    val icon: ImageVector,
    val tintColor: Color
)

/**
 * Represents exam notifications and content updates.
 */
data class UpdateNotification(
    val id: String,
    val title: String,
    val description: String,
    val tag: String,
    val date: String,
    val isNew: Boolean = false
)

/**
 * Represents individual chapter headings for Chhattisgarh Other Info (छत्तीसगढ़ अन्य जानकारी).
 * Designed for future Google Drive PDF/document linking.
 */
data class AnyaJankariChapter(
    val id: Int,
    val chapterNumber: String,
    val title: String,
    val driveUrl: String = ""
)

/**
 * Represents individual chapter headings for Chhattisgarh Schemes & Firsts (छत्तीसगढ़ योजना एवं प्रथम).
 * Designed for future Google Drive PDF/document linking.
 */
data class SchemeChapter(
    val id: Int,
    val chapterNumber: String,
    val title: String,
    val driveUrl: String = ""
)

/**
 * Represents individual chapter headings for Chhattisgarh History (छत्तीसगढ़ इतिहास).
 * Designed for future Google Drive PDF/document linking.
 */
data class HistoryChapter(
    val id: Int,
    val chapterNumber: String,
    val title: String,
    val driveUrl: String = ""
)

/**
 * Represents individual chapter headings for Chhattisgarh Polity / Rajvyavastha (छत्तीसगढ़ राज्यव्यवस्था).
 * Designed for future Google Drive PDF/document linking.
 */
data class PolityChapter(
    val id: Int,
    val chapterNumber: String,
    val title: String,
    val driveUrl: String = ""
)

/**
 * Represents individual chapter headings for Chhattisgarh Art and Culture (छत्तीसगढ़ कला एवं संस्कृति).
 * Designed for future Google Drive PDF/document linking.
 */
data class CultureChapter(
    val id: Int,
    val chapterNumber: String,
    val title: String,
    val driveUrl: String = ""
)

/**
 * Represents individual chapter headings for Chhattisgarh Geography (छत्तीसगढ़ भूगोल).
 * Designed for future Google Drive PDF/document linking.
 */
data class GeographyChapter(
    val id: Int,
    val chapterNumber: String,
    val title: String,
    val driveUrl: String = ""
)

object StudyDataProvider {

    val anyaJankariChapters = listOf(
        AnyaJankariChapter(
            id = 1,
            chapterNumber = "01",
            title = "छत्तीसगढ़ी शब्दकोष",
            driveUrl = "https://drive.google.com/uc?export=download&id=1HwRANkbaF4xH4Ud118VpGIO0qMv75Vlk"
        ),
        AnyaJankariChapter(
            id = 2,
            chapterNumber = "02",
            title = "छत्तीसगढ़ : प्रमुख व्यंजन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1RyJEQ_bsUZ6OTKmVxrMOqP_3bjgT0Cs1"
        ),
        AnyaJankariChapter(
            id = 3,
            chapterNumber = "03",
            title = "छत्तीसगढ़ : प्रमुख आभूषण",
            driveUrl = "https://drive.google.com/uc?export=download&id=1a5jn639tPhNaEOU2FzAqGGpys3kfZVNa"
        ),
        AnyaJankariChapter(
            id = 4,
            chapterNumber = "04",
            title = "छत्तीसगढ़ : प्रमुख संस्थान",
            driveUrl = "https://drive.google.com/uc?export=download&id=1axhjstePgICV3HqIf3t9LNagTy1JCL5G"
        ),
        AnyaJankariChapter(
            id = 5,
            chapterNumber = "05",
            title = "छत्तीसगढ़ : व्यक्तियों के उपनाम",
            driveUrl = "https://drive.google.com/uc?export=download&id=1KyAcvGxuVBxUghzMsjA2Q0MIzQfIq0uG"
        ),
        AnyaJankariChapter(
            id = 6,
            chapterNumber = "06",
            title = "छत्तीसगढ़ : शहरों के उपनाम",
            driveUrl = "https://drive.google.com/uc?export=download&id=1pJs-OvsbDd2EkGDMCDXuQ7gXYRAeWqqf"
        )
    )

    val schemeChapters = listOf(
        SchemeChapter(
            id = 1,
            chapterNumber = "01",
            title = "लोक कल्याणकारी योजनाएं",
            driveUrl = "https://drive.google.com/uc?export=download&id=17RQxqhIiCDz1y0EyfY5B4onSCkemGqG1"
        ),
        SchemeChapter(
            id = 2,
            chapterNumber = "02",
            title = "तिथि क्रमावली",
            driveUrl = "https://drive.google.com/uc?export=download&id=16iySTRShSienTtoPzwWKglU5w889dkEr"
        ),
        SchemeChapter(
            id = 3,
            chapterNumber = "03",
            title = "छत्तीसगढ़ राज्य में प्रथम",
            driveUrl = "https://drive.google.com/uc?export=download&id=1TCSpBIK8tSXfumaiZtnsNy8oKNa41x2L"
        )
    )

    val polityChapters = listOf(
        PolityChapter(
            id = 1,
            chapterNumber = "01",
            title = "राज्य का प्रशासनिक ढांचा",
            driveUrl = "https://drive.google.com/uc?export=download&id=1WLeEURDmfwUL7zWC9irSr9ZOR-jTQkHb"
        ),
        PolityChapter(
            id = 2,
            chapterNumber = "02",
            title = "राज्य की शासन व्यवस्था",
            driveUrl = "https://drive.google.com/uc?export=download&id=11gVBs1CEpXK5FYsVCSimIttKw76_wkLo"
        ),
        PolityChapter(
            id = 3,
            chapterNumber = "03",
            title = "राज्य के लोकसभा निर्वाचन क्षेत्र",
            driveUrl = "https://drive.google.com/uc?export=download&id=1bhGIhedoqWPXEJBX7kTY7mxbBuqIRU8K"
        ),
        PolityChapter(
            id = 4,
            chapterNumber = "04",
            title = "राज्य के विधानसभा निर्वाचन क्षेत्र",
            driveUrl = "https://drive.google.com/uc?export=download&id=1hKTmH17yaF4IIN2WNFTHNuaizUGtbq5X"
        ),
        PolityChapter(
            id = 5,
            chapterNumber = "05",
            title = "राज्य का प्रथम मंत्रिमंडल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1yI1tv_57F2hHca4oTLqjX-NheHtOP9wU"
        ),
        PolityChapter(
            id = 6,
            chapterNumber = "06",
            title = "राजकीय प्रतीक",
            driveUrl = "https://drive.google.com/uc?export=download&id=1g2_7d7jxxOdGy-UCDcf0NPDkXsEW-hci"
        ),
        PolityChapter(
            id = 7,
            chapterNumber = "07",
            title = "स्थानीय शासन : नगरीय शासन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1GEw7LBJCvk-kLhXpf88TmNeSs75rSEvo"
        ),
        PolityChapter(
            id = 8,
            chapterNumber = "08",
            title = "स्थानीय शासन : पंचायती राज",
            driveUrl = "https://drive.google.com/uc?export=download&id=1x_sbimRBqZYt8j78jHfQ6HL9Jc7CRbI7"
        ),
        PolityChapter(
            id = 9,
            chapterNumber = "09",
            title = "राज्यव्यवस्था ALL MCQ",
            driveUrl = "https://drive.google.com/uc?export=download&id=1gRr91P2GS0OcP__RmC11ZY9G_NH9WnBO"
        )
    )

    val cultureChapters = listOf(
        CultureChapter(
            id = 1,
            chapterNumber = "01",
            title = "जनजातियां",
            driveUrl = "https://drive.google.com/uc?export=download&id=1lucSB0h11zOX5WBSAZ90xD04QMwkmunu"
        ),
        CultureChapter(
            id = 2,
            chapterNumber = "02",
            title = "जनजातीय कलाएं",
            driveUrl = "https://drive.google.com/uc?export=download&id=1s-qq09_DcDAWW2bWPVJz2Hhf_tZMSzN8"
        ),
        CultureChapter(
            id = 3,
            chapterNumber = "03",
            title = "पर्व-त्योहार एवं मेले",
            driveUrl = "https://drive.google.com/uc?export=download&id=1nnCLB7fnI-aRlWznPHxmo4aFfmn1Vdvx"
        ),
        CultureChapter(
            id = 4,
            chapterNumber = "04",
            title = "भाषाएं एवं बोलियां",
            driveUrl = "https://drive.google.com/uc?export=download&id=1P2bszJDLFeCV73oJ0oi2-kAinYKJYNVm"
        ),
        CultureChapter(
            id = 5,
            chapterNumber = "05",
            title = "पर्यटन स्थल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1Xoar8FbsV_79r48_IOrHvSrT2E6cVeDa"
        ),
        CultureChapter(
            id = 6,
            chapterNumber = "06",
            title = "जनसंचार के साधन",
            driveUrl = "https://drive.google.com/uc?export=download&id=15f-njVs5LBjtGUqXear-gnudLDuXe8uQ"
        ),
        CultureChapter(
            id = 7,
            chapterNumber = "07",
            title = "विशिष्ट सम्मान एवं पुरस्कार",
            driveUrl = "https://drive.google.com/uc?export=download&id=1HM8ZuKasvJ-w752jUW9tM3Pu-mpoC8wX"
        ),
        CultureChapter(
            id = 8,
            chapterNumber = "08",
            title = "भाषा एवं साहित्य",
            driveUrl = "https://drive.google.com/uc?export=download&id=1kj07FybDX2FBhVlqbs1R8coI7jrvppSn"
        ),
        CultureChapter(
            id = 9,
            chapterNumber = "09",
            title = "छत्तीसगढ़ में शिक्षा",
            driveUrl = "https://drive.google.com/uc?export=download&id=10Ai0EyOrqcg1CdfUCeIx0H09p0aWRZBY"
        ),
        CultureChapter(
            id = 10,
            chapterNumber = "10",
            title = "प्रमुख व्यक्तित्व",
            driveUrl = "https://drive.google.com/uc?export=download&id=1HZeGp8uci2Dp0RF0j2eH0R10lkO1UBrM"
        ),
        CultureChapter(
            id = 11,
            chapterNumber = "11",
            title = "खेलकूद",
            driveUrl = "https://drive.google.com/uc?export=download&id=1xM7eh6D5ywCdCU_3IjM921f_uqerHbIX"
        )
    )

    val geographyChapters = listOf(
        GeographyChapter(
            id = 1,
            chapterNumber = "01",
            title = "स्थिति एवं विस्तार",
            driveUrl = "https://drive.google.com/uc?export=download&id=17TigyVWyduibqRdn455sHszfY6OUPR-q"
        ),
        GeographyChapter(
            id = 2,
            chapterNumber = "02",
            title = "भौतिक स्वरूप",
            driveUrl = "https://drive.google.com/uc?export=download&id=13bK-2k2D3Fx-UmUeadULU2CFVOju73Os"
        ),
        GeographyChapter(
            id = 3,
            chapterNumber = "03",
            title = "जलवायु",
            driveUrl = "https://drive.google.com/uc?export=download&id=1FViSPMd9w-FkcnZJH-C6bWSXW7Ph-bo_"
        ),
        GeographyChapter(
            id = 4,
            chapterNumber = "04",
            title = "प्राकृतिक वनस्पति",
            driveUrl = "https://drive.google.com/uc?export=download&id=1TzFTfyHi5NvVw4PL6cbO_-Jjm14x_vY0"
        ),
        GeographyChapter(
            id = 5,
            chapterNumber = "05",
            title = "वन्य जीवन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1s7AxfXrl8vuyy2Fdmg_yfJ4F8zzY1w_Y"
        ),
        GeographyChapter(
            id = 6,
            chapterNumber = "06",
            title = "नदियां एवं जलप्रपात",
            driveUrl = "https://drive.google.com/uc?export=download&id=1jJaKgspVrEQDPNYJfSNYd_UhqC3iOaXb"
        ),
        GeographyChapter(
            id = 7,
            chapterNumber = "07",
            title = "मिट्टी",
            driveUrl = "https://drive.google.com/uc?export=download&id=1KhxeXp30ntjSieSL-HFtgNQZUbJtpSYB"
        ),
        GeographyChapter(
            id = 8,
            chapterNumber = "08",
            title = "सिंचाई",
            driveUrl = "https://drive.google.com/uc?export=download&id=1eEy8AOBbGqTjJQGi5AaZPZMldfKEdJPt"
        ),
        GeographyChapter(
            id = 9,
            chapterNumber = "09",
            title = "कृषि",
            driveUrl = "https://drive.google.com/uc?export=download&id=1ggfYQA7qOgAaNeF-6D5ajor3pwGm57VQ"
        ),
        GeographyChapter(
            id = 10,
            chapterNumber = "10",
            title = "खनिज संसाधन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1A3QdVnn7RuStLuMbe0zjWVYYchaDQqmP"
        ),
        GeographyChapter(
            id = 11,
            chapterNumber = "11",
            title = "उद्योग",
            driveUrl = "https://drive.google.com/uc?export=download&id=1-HKs6TSOYwkrHtog0deF6L5TMlZgj59o"
        ),
        GeographyChapter(
            id = 12,
            chapterNumber = "12",
            title = "परिवहन के साधन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1R3G86xee9pV4K9XMuxQF3oBWPcYICD78"
        ),
        GeographyChapter(
            id = 13,
            chapterNumber = "13",
            title = "जनसंख्या",
            driveUrl = "https://drive.google.com/uc?export=download&id=1HaL8avUvE8FinrEBqdIXlSoG4s-CBDx9"
        )
    )

    val historyChapters = listOf(
        HistoryChapter(
            id = 1,
            chapterNumber = "01",
            title = "परिचय व नामकरण",
            driveUrl = "https://drive.google.com/uc?export=download&id=1gngwkHJDiwnnvfnar3k2rbLU9WOKQQ-D"
        ),
        HistoryChapter(
            id = 2,
            chapterNumber = "02",
            title = "पाषाण काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1xkOfdImri_-VzBwaq_00H5KypYLsrij_"
        ),
        HistoryChapter(
            id = 3,
            chapterNumber = "03",
            title = "वैदिक काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=12sXUBD4zgdD3KhDvVN5M1exLF5pvyYfi"
        ),
        HistoryChapter(
            id = 4,
            chapterNumber = "04",
            title = "महाकाव्य काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1AIVWCOUnzXoZqDo-Mk77uQ0vLNyUeVWP"
        ),
        HistoryChapter(
            id = 5,
            chapterNumber = "05",
            title = "महाजनपद काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1MI2PJjZjEDKSFBprcl65lJIFFEnxhIH1"
        ),
        HistoryChapter(
            id = 6,
            chapterNumber = "06",
            title = "मौर्य काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1NIdaDqu-k8w__EWhKAmXuSdHbCDrLtYm"
        ),
        HistoryChapter(
            id = 7,
            chapterNumber = "07",
            title = "सातवाहन काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1niD4klzx3oVQ90CoW38dUe4Wxz3SU1Ec"
        ),
        HistoryChapter(
            id = 8,
            chapterNumber = "08",
            title = "वाकाटक काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1msQkVXFLviqtMwGPUjkn1Y_0yAEKmmRH"
        ),
        HistoryChapter(
            id = 9,
            chapterNumber = "09",
            title = "गुप्त काल",
            driveUrl = "https://drive.google.com/uc?export=download&id=1M1wia_kwW6O3T0I6B7YiWDz-HokhVEdQ"
        ),
        HistoryChapter(
            id = 10,
            chapterNumber = "10",
            title = "क्षेत्रीय राजवंश",
            driveUrl = "https://drive.google.com/uc?export=download&id=1gmzE8ceOpYA6KU1rOjoMHOIxeq7-lJJr"
        ),
        HistoryChapter(
            id = 11,
            chapterNumber = "11",
            title = "कल्चुरियों का काल : I. रतनपुर के कल्चुरी II. रायपुर के कल्चुरी",
            driveUrl = "https://drive.google.com/uc?export=download&id=1vu0BBrBTPRoUajDS2W2J5s3YjIDLCJQJ"
        ),
        HistoryChapter(
            id = 12,
            chapterNumber = "12",
            title = "मराठा शासन : I. प्रत्यक्ष भोंसला शासन II. सूबा शासन (सूवा सरकार) III. ब्रिटिश संरक्षणाधीन मराठा शासन IV. पुनः भोंसला शासन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1oewWlBXYH8WKOIhxh_aFtqFEiW1G6Kyb"
        ),
        HistoryChapter(
            id = 13,
            chapterNumber = "13",
            title = "ब्रिटिश शासन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1hgGlSdnSbvjPFVa2NkWenJGvkTu_Xddh"
        ),
        HistoryChapter(
            id = 14,
            chapterNumber = "14",
            title = "स्वतंत्रता आंदोलन : I. राष्ट्रीय आंदोलन II. जनजातीय/ आदिवासी आंदोलन III. किसान आंदोलन IV. मजदूर आंदोलन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1JBTTQcYPjfsfBrw2wG0Rj7PwkzfGhMsg"
        ),
        HistoryChapter(
            id = 15,
            chapterNumber = "15",
            title = "छत्तीसगढ़ राज्य निर्माण आंदोलन",
            driveUrl = "https://drive.google.com/uc?export=download&id=1r8CKZVN5iIU93uAczfsfealHbJe2yCd0"
        ),
        HistoryChapter(
            id = 16,
            chapterNumber = "16",
            title = "महत्वपूर्ण तिथियाँ",
            driveUrl = "https://drive.google.com/uc?export=download&id=1rZVGWow2kuBic29wat3hSJYbMtCIQ9JZ"
        )
    )

    val mainCategories = listOf(
        StudyCategory(
            id = "history",
            titleHindi = "छत्तीसगढ़ इतिहास",
            titleEnglish = "Chhattisgarh History",
            subtitle = "प्राचीन, मध्यकालीन व आधुनिक इतिहास",
            chapterCount = "16 अध्याय",
            icon = Icons.Default.HistoryEdu,
            iconBgColor = CgCatHistoryBg,
            iconTintColor = CgCatHistoryIcon,
            topics = listOf(
                "प्रागैतिहासिक काल एवं शैलचित्र",
                "कलचुरी वंश एवं मराठा शासन",
                "ब्रिटिश नियंत्रण एवं 1857 की क्रांति",
                "छत्तीसगढ़ के प्रमुख जनजातीय विद्रोह",
                "स्वतंत्रता आंदोलन में छत्तीसगढ़ का योगदान"
            )
        ),
        StudyCategory(
            id = "geography",
            titleHindi = "छत्तीसगढ़ भूगोल",
            titleEnglish = "Chhattisgarh Geography",
            subtitle = "नदी अपवाह, खनिज, वन व जलवायु",
            chapterCount = "13 अध्याय",
            icon = Icons.Default.Landscape,
            iconBgColor = CgCatGeographyBg,
            iconTintColor = CgCatGeographyIcon,
            topics = listOf(
                "भौगोलिक स्थिति एवं प्राकृतिक विभाजन",
                "महानदी व अन्य नदी अपवाह तंत्र",
                "खनिज संसाधन एवं प्रमुख उद्योग",
                "राष्ट्रीय उद्यान एवं वन्यजीव अभयारण्य",
                "मिट्टी, जलवायु एवं कृषि प्रारूप"
            )
        ),
        StudyCategory(
            id = "culture",
            titleHindi = "कला एवं संस्कृति",
            titleEnglish = "Art & Culture",
            subtitle = "जनजातियां, लोकनृत्य, पर्व व भाषा",
            chapterCount = "11 अध्याय",
            icon = Icons.Default.TheaterComedy,
            iconBgColor = CgCatCultureBg,
            iconTintColor = CgCatCultureIcon,
            topics = listOf(
                "छत्तीसगढ़ की प्रमुख जनजातियां एवं परंपराएं",
                "लोक नृत्य (करमा, सुआ, पंथी, राउत नाचा)",
                "प्रमुख लोक पर्व (हरेली, तीजा, बस्तर दशहरा)",
                "छत्तीसगढ़ी बोली, साहित्य एवं रचनाकार",
                "शिल्पकला (ढोकरा कला, लौह शिल्प)"
            )
        ),
        StudyCategory(
            id = "polity",
            titleHindi = "राज्यव्यवस्था",
            titleEnglish = "Polity & Governance",
            subtitle = "प्रशासन, पंचायती राज व विधानसभा",
            chapterCount = "9 अध्याय",
            icon = Icons.Default.AccountBalance,
            iconBgColor = CgCatPolityBg,
            iconTintColor = CgCatPolityIcon,
            topics = listOf(
                "छत्तीसगढ़ राज्य गठन एवं प्रशासनिक संरचना",
                "राज्यपाल, मुख्यमंत्री एवं मंत्रिपरिषद",
                "छत्तीसगढ़ विधानसभा एवं उच्च न्यायालय",
                "पंचायती राज व्यवस्था एवं नगरीय निकाय",
                "संवैधानिक एवं सांविधिक आयोग"
            )
        ),
        StudyCategory(
            id = "schemes",
            titleHindi = "योजना और प्रथम",
            titleEnglish = "Schemes & First in CG",
            subtitle = "शासकीय योजनाएं व प्रथम व्यक्तित्व",
            chapterCount = "3 अध्याय",
            icon = Icons.Default.EmojiEvents,
            iconBgColor = CgCatSchemesBg,
            iconTintColor = CgCatSchemesIcon,
            topics = listOf(
                "छत्तीसगढ़ शासन की फ्लैगशिप योजनाएं",
                "छत्तीसगढ़ में प्रथम व्यक्ति व पद",
                "राज्य अलंकरण एवं प्रमुख पुरस्कार",
                "कृषि, शिक्षा व स्वास्थ्य संबंधी योजनाएं",
                "महिला एवं बाल विकास कार्यक्रम"
            )
        ),
        StudyCategory(
            id = "misc",
            titleHindi = "अन्य जानकारी",
            titleEnglish = "General Studies & Misc",
            subtitle = "प्रतीक चिन्ह, जनगणना व विविध तथ्य",
            chapterCount = "6 अध्याय",
            icon = Icons.Default.Star,
            iconBgColor = CgCatMiscBg,
            iconTintColor = CgCatMiscIcon,
            topics = listOf(
                "राज्य प्रतीक चिन्ह (पशु, पक्षी, वृक्ष, गीत)",
                "छत्तीसगढ़ जनगणना 2011 सांख्यिकी",
                "प्रमुख शोध संस्थान, विश्वविद्यालय एवं अकादमियां",
                "प्रमुख बांध, जलप्रपात एवं पर्यटन स्थल",
                "महत्वपूर्ण समसामयिक तथ्य"
            )
        )
    )

    val quickAccessList = listOf(
        QuickAccessItem(
            id = "notes_pdf",
            titleHindi = "नोट्स PDF",
            titleEnglish = "Study Notes",
            badge = "PDF",
            icon = Icons.Default.MenuBook,
            tintColor = Color(0xFF0284C7)
        ),
        QuickAccessItem(
            id = "imp_topics",
            titleHindi = "महत्वपूर्ण बिंदु",
            titleEnglish = "Key Topics",
            badge = "Quick",
            icon = Icons.Default.AutoStories,
            tintColor = Color(0xFFD97706)
        ),
        QuickAccessItem(
            id = "pyq",
            titleHindi = "पिछले वर्षों के प्रश्न",
            titleEnglish = "Previous PYQs",
            badge = "PYQ",
            icon = Icons.Default.Quiz,
            tintColor = Color(0xFF7C3AED)
        ),
        QuickAccessItem(
            id = "exam_prep",
            titleHindi = "परीक्षा रणनीति",
            titleEnglish = "Exam Strategy",
            badge = "2026",
            icon = Icons.Default.Star,
            tintColor = Color(0xFF059669)
        )
    )

    val latestUpdates = listOf(
        UpdateNotification(
            id = "1",
            title = "CGPSC राज्य सेवा परीक्षा 2026",
            description = "प्रारंभिक परीक्षा हेतु छत्तीसगढ़ सामान्य अध्ययन का अद्यतन सिलेबस एवं गाइड",
            tag = "CGPSC",
            date = "हाल ही में जोड़ा गया",
            isNew = true
        ),
        UpdateNotification(
            id = "2",
            title = "छत्तीसगढ़ बजट 2025-26 सार संक्षेप",
            description = "परीक्षा उपयोगी महत्वपूर्ण आर्थिक आंकड़े एवं नवीन योजनाएं सम्मिलित",
            tag = "बजट",
            date = "अद्यतन",
            isNew = true
        ),
        UpdateNotification(
            id = "3",
            title = "CG व्यापम आगामी भर्ती परीक्षाएं",
            description = "पटवारी, छात्रावास अधीक्षक, शिक्षक भर्ती हेतु विशेष वस्तुनिष्ठ प्रश्नोत्तरी",
            tag = "CG व्यापम",
            date = "अभ्यास सेट",
            isNew = false
        )
    )
}
