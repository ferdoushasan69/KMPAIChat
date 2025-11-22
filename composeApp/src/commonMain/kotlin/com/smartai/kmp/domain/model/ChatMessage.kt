package com.smartai.kmp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val messageId: String,
    val groupId: String,
    val text: String="Hi gemini",
    val images: List<ByteArray> = emptyList(),
    val participant: Role = Role.YOU,
    val isPending: Boolean = false
)
