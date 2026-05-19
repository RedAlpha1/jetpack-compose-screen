package com.learning.jetpack_compose_practice.screens.day3.data




import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.learning.jetpack_compose_practice.ui.theme.BgDark
import com.learning.jetpack_compose_practice.ui.theme.GreenLight
import com.learning.jetpack_compose_practice.ui.theme.GreenMain
import com.learning.jetpack_compose_practice.ui.theme.Purple
import com.learning.jetpack_compose_practice.ui.theme.PurpleLight
import com.learning.jetpack_compose_practice.ui.theme.RedMain

data class OnboardingPage(
    val slideNumber : String,
    val titleStart  : String,       // "Goals that\n"
    val accentWord  : String,       // "feel alive."
    val subtitle    : String,
    val accentColor : Color,        // colored word in title
    val primaryColor: Color,        // button + progress bar
    val btnTextColor: Color = Color.White,
    val artIcon     : ImageVector,
    val artIconTint : Color,
)

val pages = listOf(
    OnboardingPage(
        slideNumber  = "01 / 03",
        titleStart   = "Goals that\n",
        accentWord   = "feel alive.",
        subtitle     = "Set targets that adapt to your life — not the other way around.",
        accentColor  = PurpleLight,
        primaryColor = Purple,
        artIcon      = Icons.Outlined.AutoAwesome,
        artIconTint  = BgDark,
    ),
    OnboardingPage(
        slideNumber  = "02 / 03",
        titleStart   = "Progress you\n",
        accentWord   = "can see.",
        subtitle     = "Real numbers, real streaks, real momentum — no fluff.",
        accentColor  = GreenLight,
        primaryColor = GreenMain,
        btnTextColor = Color(0xFF08080A),   // dark text on light green button
        artIcon      = Icons.Outlined.TrendingUp,
        artIconTint  = GreenLight,
    ),
    OnboardingPage(
        slideNumber  = "03 / 03",
        titleStart   = "Better,\n",
        accentWord   = "together.",
        subtitle     = "Challenge friends, share wins, and build accountability that lasts.",
        accentColor  = RedMain,
        primaryColor = RedMain,
        artIcon      = Icons.Outlined.Favorite,
        artIconTint  = RedMain,
    ),

)
