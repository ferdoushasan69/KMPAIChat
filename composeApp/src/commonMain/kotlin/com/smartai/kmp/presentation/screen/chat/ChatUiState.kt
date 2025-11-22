package com.smartai.kmp.presentation.screen.chat

import com.smartai.kmp.domain.model.ChatMessage

data class ChatUiState(
    val message : List<ChatMessage> = emptyList(),
    val isApiLoading : Boolean = false,
    val isLoading : Boolean = false
)
