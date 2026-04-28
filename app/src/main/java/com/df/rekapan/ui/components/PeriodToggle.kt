package com.df.rekapan.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.df.rekapan.ui.theme.NavyPrimary
import com.df.rekapan.ui.theme.PoppinsFamily
import com.df.rekapan.ui.theme.RekapanTheme
import com.df.rekapan.ui.theme.RubikFamily

/** Represents the active reporting period shown on the Home screen. */
enum class Period { WEEK, MONTH }

/**
 * A two-option segmented control that animates a sliding pill indicator
 * between [Period.WEEK] ("Minggu ini") and [Period.MONTH] ("Bulan Ini").
 *
 * The pill offset is driven by [animateDpAsState] with a low-stiffness spring
 * so the transition feels smooth without any overshoot.
 *
 * @param selectedPeriod   The currently active period.
 * @param onPeriodSelected Callback invoked when the user taps a segment.
 * @param modifier         Applied to the outer track container.
 * @param trackColor       Background color of the track.
 * @param activeColor      Fill color of the sliding pill.
 * @param activeTextColor  Label color for the selected segment.
 * @param inactiveTextColor Label color for the unselected segment.
 * @param height           Total height of the control.
 */
@Composable
fun PeriodToggle(
    selectedPeriod: Period,
    onPeriodSelected: (Period) -> Unit,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.White,
    activeColor: Color = NavyPrimary,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = NavyPrimary,
    height: Dp = 46.dp,
    horizontalPadding: Dp = 5.dp,
    verticalPadding: Dp = 5.dp,
) {
    val density = LocalDensity.current
    var trackWidthPx by remember { mutableIntStateOf(0) }

    // Pill slides to the right half when MONTH is selected.
    // We subtract horizontalPadding to keep the pill within the padded track bounds.
    val sliderOffset by animateDpAsState(
        targetValue = if (selectedPeriod == Period.WEEK) 0.dp
                      else with(density) { (trackWidthPx / 2).toDp() } - horizontalPadding,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness    = Spring.StiffnessLow
        ),
        label = "sliderOffset"
    )

    Box(
        modifier = modifier
            .height(height)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(30))
            .clip(RoundedCornerShape(30))
            .background(trackColor)
            .onGloballyPositioned { trackWidthPx = it.size.width }
    ) {
        // Animated sliding pill — renders below the label row so labels remain tappable
        Box(
            modifier = Modifier
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .offset(x = sliderOffset)
                .clip(RoundedCornerShape(30))
                .background(activeColor)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PeriodTab(
                label         = "Minggu ini",
                isActive      = selectedPeriod == Period.WEEK,
                activeColor   = activeTextColor,
                inactiveColor = inactiveTextColor,
                modifier      = Modifier.weight(1f),
                onClick       = { onPeriodSelected(Period.WEEK) }
            )
            PeriodTab(
                label         = "Bulan Ini",
                isActive      = selectedPeriod == Period.MONTH,
                activeColor   = activeTextColor,
                inactiveColor = inactiveTextColor,
                modifier      = Modifier.weight(1f),
                onClick       = { onPeriodSelected(Period.MONTH) }
            )
        }
    }
}

@Composable
private fun PeriodTab(
    label: String,
    isActive: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        targetValue   = if (isActive) activeColor else inactiveColor,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label         = "tabTextColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null, // visual feedback is the pill itself
                onClick           = onClick
            )
    ) {
        Text(
            text       = label,
            fontFamily = PoppinsFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 14.sp,
            color      = textColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PeriodTogglePreview() {
    RekapanTheme {
        var selected by remember { mutableStateOf(Period.WEEK) }
        PeriodToggle(
            selectedPeriod   = selected,
            onPeriodSelected = { selected = it },
            modifier         = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}
