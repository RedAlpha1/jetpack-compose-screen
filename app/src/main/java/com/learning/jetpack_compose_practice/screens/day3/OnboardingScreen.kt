import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.jetpack_compose_practice.screens.day3.data.OnboardingPage
import com.learning.jetpack_compose_practice.screens.day3.data.pages
import com.learning.jetpack_compose_practice.ui.theme.BgDark
import com.learning.jetpack_compose_practice.ui.theme.DarkBg
import com.learning.jetpack_compose_practice.ui.theme.GreenDark
import com.learning.jetpack_compose_practice.ui.theme.GreenLight
import com.learning.jetpack_compose_practice.ui.theme.GreenMain
import com.learning.jetpack_compose_practice.ui.theme.Purple
import com.learning.jetpack_compose_practice.ui.theme.PurpleDark
import com.learning.jetpack_compose_practice.ui.theme.PurpleLight
import com.learning.jetpack_compose_practice.ui.theme.RedDark
import com.learning.jetpack_compose_practice.ui.theme.RedMain
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen() {

    // PagerState
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    // AnimateButton + bar color as page changes
    val animateBtn by animateColorAsState(
        targetValue = pages[currentPage].primaryColor,
        animationSpec = tween(durationMillis = 500),
        label = "btnColor"
    )
    val animateBtnTextColor by animateColorAsState(
        targetValue = pages[currentPage].btnTextColor,
        animationSpec = tween(durationMillis = 500),
        label = "btnTextColor"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .systemBarsPadding()

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                key = { it }
            ) { page ->
                OnBoardingSlide(
                    page = pages[page],
                    pageIndex = page
                )
            }

            // Bottom Controls
            BottomSections(
                currentPage = currentPage,
                btnColor = animateBtn,
                btnTextColor = animateBtnTextColor,
                totalPages = pages.size,
                onSkip = {
                    scope.launch {
                        pagerState.animateScrollToPage(pages.size - 1)
                    }
                },
                onContinue = {
                    scope.launch {
                        pagerState.animateScrollToPage(currentPage + 1)
                    }
                }
            )
        }
    }


}

@Composable
fun OnBoardingSlide(page: OnboardingPage, pageIndex: Int) {
    Column(modifier = Modifier.fillMaxSize().background(BgDark))  {
        Box(modifier = Modifier
            .height(380.dp)
            .fillMaxWidth()
            .background(BgDark),
            contentAlignment = Alignment.Center
        ) {
            when(pageIndex) {
                0 -> SlideOneArt(icon = page.artIcon, tint = page.artIconTint)
                1 -> SlideTwoArt(icon = page.artIcon, tint = page.artIconTint)
                else -> SlideThreeArt(icon = page.artIcon, tint = page.artIconTint)
            }
        }

        HorizontalDivider(
            modifier  = Modifier.fillMaxWidth(),
            thickness = 0.5.dp,
            color     = Color.White.copy(alpha = 0.08f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .padding(top = 28.dp)
        ) {
            // "01 / 03"
            Text(
                text = page.slideNumber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                color = Color.White.copy(alpha = 0.25f)
            )
            Spacer(modifier = Modifier.height(14.dp))

            // "Goals that\nfeel alive."  ← accent word colored
            Text(
                text = buildAnnotatedString {
                    append(page.titleStart)
                    withStyle(SpanStyle(color = page.accentColor)) {
                        append(page.accentWord)
                    }
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 38.sp,
                letterSpacing = (-0.5).sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = page.subtitle,
                fontSize = 14.sp,
                lineHeight = 23.sp,
                color = Color.White.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun SlideOneArt(icon: ImageVector, tint: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Large square — top left
        Box(
            modifier = Modifier
                .size(170.dp)
                .offset(x = 30.dp, y = 60.dp)
                .rotate(-12f)
                .clip(RoundedCornerShape(20.dp))
                .background(Purple)
        )
        // Medium square — center
        Box(
            modifier = Modifier
                .size(110.dp)
                .offset(x = 120.dp, y = 80.dp)
                .rotate(8f)
                .clip(RoundedCornerShape(20.dp))
                .background(PurpleLight)
        )
        // Small dark square — bottom right
        Box(
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-50).dp, y = (-70).dp)
                .rotate(20f)
                .clip(RoundedCornerShape(12.dp))
                .background(PurpleDark)
        )
        // Outline square — bottom left
        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.BottomStart)
                .offset(x = 40.dp, y = (-90).dp)
                .rotate(-5f)
                .border(2.dp, Purple.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
        )
        // Center icon
        Icon(
            imageVector     = icon,
            contentDescription = null,
            tint            = tint,
            modifier        = Modifier.size(48.dp).align(Alignment.Center)
        )
    }


}

// Slide 2 — Circles / Green
@Composable
private fun SlideTwoArt(icon: ImageVector, tint: Color) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Large circle — top right
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-20).dp, y = 40.dp)
                .clip(CircleShape)
                .background(GreenMain)
        )
        // Rounded rect — overlapping center
        Box(
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-90).dp, y = 90.dp)
                .rotate(-15f)
                .clip(RoundedCornerShape(32.dp))
                .background(GreenLight)
        )
        // Small dark circle — bottom left
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.BottomStart)
                .offset(x = 60.dp, y = (-80).dp)
                .clip(CircleShape)
                .background(GreenDark)
        )
        // Outline circle — bottom right
        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-30).dp, y = (-60).dp)
                .border(2.dp, GreenMain.copy(alpha = 0.25f), CircleShape)
        )
        // Center icon
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = tint,
            modifier           = Modifier.size(48.dp).align(Alignment.Center)
        )
    }
}

@Composable
private fun SlideThreeArt(icon: ImageVector, tint: Color) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Large ellipse — top right
        Box(
            modifier = Modifier
                .size(width = 160.dp, height = 136.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-30).dp, y = 50.dp)
                .clip(CircleShape)
                .background(RedMain)
        )
        // Rounded rect — top left
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopStart)
                .offset(x = 40.dp, y = 100.dp)
                .rotate(22f)
                .clip(RoundedCornerShape(24.dp))
                .background(RedMain.copy(alpha = 0.7f))
        )
        // Small dark circle — bottom right
        Box(
            modifier = Modifier
                .size(55.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-80).dp, y = (-80).dp)
                .clip(CircleShape)
                .background(RedDark)
        )
        // Outline rect — bottom left
        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.BottomStart)
                .offset(x = 50.dp, y = (-60).dp)
                .rotate(-10f)
                .border(2.dp, RedMain.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
        )
        // Center icon
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = tint,
            modifier           = Modifier.size(48.dp).align(Alignment.Center)
        )
    }
}

@Composable
private fun BottomSections(
    currentPage  : Int,
    totalPages   : Int,
    btnColor     : Color,
    btnTextColor : Color,
    onSkip       : () -> Unit,
    onContinue   : () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgDark)
            .padding(horizontal = 26.dp)
            .padding(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ── Progress segments ─────────────────────────────────
        SegmentedProgressBar(
            currentPage  = currentPage,
            totalPages   = totalPages,
            activeColor  = btnColor
        )

        // ── Button row ────────────────────────────────────────
        if (currentPage == totalPages - 1) {
            // Last page → full width "Let's go" button
            Button(
                onClick  = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnColor,
                    contentColor   = btnTextColor
                )
            ) {
                Text(
                    text= "Let's go",
                    fontSize= 15.sp,
                    fontWeight=FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier           = Modifier.size(16.dp)
                )
            }
        } else {
            // Skip + Continue
            Row(
                modifier= Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text     = "Skip",
                    fontSize = 13.sp,
                    color    = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.clickable(
                        indication         = null,
                        interactionSource  = remember { MutableInteractionSource() },
                        onClick            = onSkip
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick  = onContinue,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape  = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnColor,
                        contentColor   = btnTextColor
                    )
                ) {
                    Text(
                        text       = "Continue",
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier           = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SegmentedProgressBar(
    currentPage : Int,
    totalPages  : Int,
    activeColor : Color,
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalPages) { index ->
            val isActive = index == currentPage
            val isDone   = index < currentPage

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isDone || isActive) activeColor
                        else Color.White.copy(alpha = 0.1f)
                    )
            )
        }
    }
}


