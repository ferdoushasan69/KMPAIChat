package com.smartai.kmp.presentation.screen.chat

import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartai.kmp.presentation.component.MessageItem
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.setValue
import com.smartai.kmp.utils.API_KEY

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = koinViewModel(),
) {
    val uiState = viewModel.chatUiState.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseOutSine
            )
        ), label = ""
    )
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

    LazyColumn(
        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.outlineVariant),
        viewModel.lazyListState,
        reverseLayout = true,
        verticalArrangement = if (uiState.value.message.isEmpty()) Arrangement.Center else Arrangement.Bottom,
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) {
        if (uiState.value.message.isNotEmpty()) {
            items(uiState.value.message, key = {
                it.messageId
            }) {
                MessageItem(it, rotate)
            }
        } else {
            item {
                Box(Modifier.fillMaxSize()) {
                    Text("Welcome")
                }
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            singleLine = true,
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f).padding(8.dp)
        )
        IconButton(onClick = {
            viewModel.generateContentWithText(
                content = text,
                apiKey = API_KEY,
                groupId = viewModel.groupId
            )
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
        }
    }
    }

}