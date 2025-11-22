package com.smartai.kmp.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import com.smartai.kmp.TextComposable
import com.smartai.kmp.domain.model.ChatMessage
import com.smartai.kmp.domain.model.Role
import com.smartai.kmp.toComposeImageBitmap


@Composable
fun MessageItem(
    chatMessage: ChatMessage,
    rotate: Float
) {

    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val isGeminiMessage = chatMessage.participant != Role.YOU
    val circleColors = listOf(
        Color(0xFF5851D8),
        Color(0xFF833AB4),
        Color(0xFFC13584),
        Color(0xFFE1306C),
        Color(0xFFFD1D1D),
        Color(0xFFF56040),
        Color(0xFFF77737),
        Color(0xFFFCAF45),
        Color(0xFFFFDC80),
        Color(0xFF5851D8)
    )
    val borderColor = Color(0xFF1A30A7)
    val backgroundColor = when (chatMessage.participant) {
        Role.GEMINI -> borderColor
        Role.YOU -> borderColor
        Role.ERROR -> Color.Red
    }
    val cardShape = if (isGeminiMessage) {
        RoundedCornerShape(
            16.dp, 16.dp, 16.dp, 0.dp
        )
    } else {
        RoundedCornerShape(
            16.dp, 16.dp, 0.dp, 16.dp
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.wrapContentSize().padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(2.dp)
                .drawWithContent {
                    if (chatMessage.isPending) {
                        rotate(rotate) {
                            drawCircle(
                                brush = Brush.sweepGradient(circleColors),
                                radius = size.width,
                                blendMode = BlendMode.SrcIn
                            )
                        }
                    }
                    drawContent()
                }.background(backgroundColor, cardShape)
        ) {
            Column(modifier = Modifier.wrapContentSize()) {
                if (chatMessage.images.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.wrapContentSize().padding(bottom = 4.dp),
                        reverseLayout = true,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        items(chatMessage.images) {
                            val bitmap = it.toComposeImageBitmap()
                            Image(
                                bitmap = bitmap,
                                contentDescription = null,
                                modifier = Modifier.heightIn(max = 192.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.FillHeight
                            )
                        }
                    }
                    TextComposable(chatMessage.text, isGeminiMessage = isGeminiMessage)
                }
            }
        }
    }
}