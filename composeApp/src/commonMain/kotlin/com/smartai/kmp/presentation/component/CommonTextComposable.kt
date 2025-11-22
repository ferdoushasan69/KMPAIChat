package com.smartai.kmp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonTextComposable(isGeminiMessage: Boolean, message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isGeminiMessage) Arrangement.Start else Arrangement.End
    ) {
        if (isGeminiMessage) {
            // Gemini avatar / icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF6200EA), shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .background(
                    if (isGeminiMessage) Color(0xFFEDE7F6) else Color(0xFFB2EBF2),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(10.dp)
        ) {
            Text(message)
        }

        if (!isGeminiMessage) {
            Spacer(modifier = Modifier.width(8.dp))
            // User avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF00838F), shape = CircleShape)
            )
        }
    }
}