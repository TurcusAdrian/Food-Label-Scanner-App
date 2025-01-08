package com.example.food_label_scanner.bottom_bar_drawer_content_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R
import com.example.food_label_scanner.data.GuidelineItem

@Composable
fun CommunityGuidelines() {
    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp)
    ) {
        Text(
            text = "Community Guidelines",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = instrument_serif
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val guidelines = listOf(
            "Be respectful to other users.",
            "Do not post offensive or inappropriate content.",
            "Do not engage in spamming or advertising.",
            "Do not share personal information of others.",
            "Follow the rules and guidelines of the community.",
            "Report any violations to the moderators."
        )

        guidelines.forEachIndexed { index, guideline ->
            GuidelineItem(guideline = guideline, index = index + 1)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}