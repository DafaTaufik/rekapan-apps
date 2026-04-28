package com.df.rekapan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.df.rekapan.ui.theme.RubikFamily

@Composable
fun DateBadge(
    dayText: String,
    dateText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0x21D1D6FF),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.5.dp,
                color = Color(0x66D1D6FF),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = dayText,
            fontFamily = RubikFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 5.sp,
            color = Color.White
        )

        Text(
            text = dateText,
            fontFamily = RubikFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            lineHeight = 5.sp,
            color = Color.White
        )
    }
}
