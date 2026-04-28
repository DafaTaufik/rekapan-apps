package com.df.rekapan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.df.rekapan.ui.dashboard.HomeScreen
import com.df.rekapan.ui.theme.RekapanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Force light (white) status bar icons to contrast against the dark navy header.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(scrim = android.graphics.Color.BLACK)
        )
        setContent {
            RekapanTheme {
                HomeScreen()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainPreview() {
    RekapanTheme {
        HomeScreen()
    }
}