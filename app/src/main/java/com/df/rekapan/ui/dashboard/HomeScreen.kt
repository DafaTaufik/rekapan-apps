package com.df.rekapan.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.df.rekapan.ui.components.DateBadge
import com.df.rekapan.ui.components.Period
import com.df.rekapan.ui.components.PeriodToggle
import com.df.rekapan.ui.theme.AccentBlue
import com.df.rekapan.ui.theme.NavyLight
import com.df.rekapan.ui.theme.NavyPrimary
import com.df.rekapan.ui.theme.PoppinsFamily
import com.df.rekapan.ui.theme.RekapanTheme
import com.df.rekapan.ui.theme.RubikFamily
import com.df.rekapan.ui.theme.SoraFamily
import com.df.rekapan.ui.theme.TextBlue

/**
 * Root screen layout composed of two layers stacked in a [Box]:
 * 1. A navy header with decorative circles drawn via [drawBehind].
 * 2. A white content card that overlaps the header with rounded top corners.
 *
 * @param headerHeightFraction Fraction of screen height occupied by the header (default 35 %).
 * @param whiteCardRadius      Corner radius applied to the top edge of the content card.
 * @param content              Slot for content rendered inside the white card.
 */
@Composable
fun HomeScreen(
    headerHeightFraction: Float = 0.35f,
    whiteCardRadius: Dp = 30.dp,
    content: @Composable BoxScope.() -> Unit = {}
) {
    var selectedPeriod by remember { mutableStateOf(Period.WEEK) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Header — navy background + decorative circles
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(headerHeightFraction + 0.04f)
                .align(Alignment.TopCenter)
                .background(NavyPrimary)
        ) {
            // Decorative circles are drawn on the canvas layer so they clip naturally
            // to the header bounds without needing an explicit clipToBounds modifier.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        val w = size.width
                        val h = size.height

                        // Left-bottom circle (115 dp diameter, per Figma)
                        drawCircle(
                            color  = NavyLight,
                            radius = 57.5.dp.toPx(),
                            center = Offset(38.dp.toPx(), h - 119.dp.toPx())
                        )

                        // Top-center circle (116 dp diameter, per Figma)
                        drawCircle(
                            color  = NavyLight,
                            radius = 58.dp.toPx(),
                            center = Offset(w * 0.5f, 40.dp.toPx())
                        )

                        // Right-edge circle (136 dp diameter, per Figma)
                        drawCircle(
                            color  = NavyLight,
                            radius = 68.dp.toPx(),
                            center = Offset(w - 10.dp.toPx(), h * 0.52f)
                        )
                    }
            )

            DateBadge(
                dayText  = "Selasa,",
                dateText = "10 Maret 2026",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 39.dp, end = 12.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 22.dp)
                    .statusBarsPadding()
                    .padding(top = 36.dp)
            ) {
                Text(
                    text       = "rekap.",
                    color      = Color.White,
                    fontFamily = SoraFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 24.sp
                )

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text       = "Selamat Pagi",
                    color      = Color.White,
                    fontFamily = RubikFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 14.sp
                )

                Text(
                    text       = "Hallo, User!",
                    color      = Color.White,
                    fontFamily = SoraFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 22.sp
                )
            }
        }

        // Content card — overlaps the header by ~6.6 % of screen height
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f - headerHeightFraction + 0.066f)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = whiteCardRadius, topEnd = whiteCardRadius)
                )
        ) {
            PeriodToggle(
                selectedPeriod   = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it },
                modifier         = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 19.dp, vertical = 16.dp)
            )

            SummaryCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 19.dp)
            )
        }
    }
}

/**
 * Summary card displayed below the period toggle.
 *
 * Two decorative circles are drawn at positions taken directly from Figma
 * (coordinates are relative to the card's top-left corner).
 * [Modifier.clip] ensures both circles are cropped to the card's rounded bounds.
 */
@Composable
private fun SummaryCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(138.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(NavyPrimary)
            .drawBehind {
                // Bottom circle — #1B6CA8 @ 35% opacity
                // Figma bounding box: x=241, y=59, size=104×104 → center=(293, 111)
                drawCircle(
                    color  = AccentBlue.copy(alpha = 0.35f),
                    radius = 52.dp.toPx(),
                    center = Offset(293.dp.toPx(), 111.dp.toPx())
                )

                // Top circle — #0F3A78 @ 48% opacity
                // Figma bounding box: x=277, y=-18, size=115×115 → center=(334.5, 39.5)
                drawCircle(
                    color  = NavyLight.copy(alpha = 0.48f),
                    radius = 57.5.dp.toPx(),
                    center = Offset(334.5.dp.toPx(), 39.5.dp.toPx())
                )
            }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text       = "TOTAL PENDAPATAN",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = Color.White
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text       = "Rp 4.280.000",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 30.sp,
                color      = Color.White
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text       = "Lunas + Belum lunas",
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 12.sp,
                color      = TextBlue
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    RekapanTheme {
        HomeScreen()
    }
}
