package com.smartai.kmp.domain.repository

import com.smartai.kmp.domain.model.ChatMessage
import com.smartai.kmp.domain.model.Gemini
import com.smartai.kmp.domain.model.Group
import com.smartai.kmp.domain.model.Role

interface GeminiRepository {

    suspend fun generateContent(content: String, apiKey: String): Gemini

    suspend fun generateContentWithImage(
        content: String,
        apiKey: String,
        images: List<ByteArray> = emptyList()
    ): Gemini


    suspend fun insertGroup(
        groupId: String,
        title: String,
        date: String,
        icon: String
    )

    suspend fun insertMessage(
        messageId: String,
        groupId: String,
        text: String,
        image: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    )

    suspend fun updatePendingStatus(
        messageId: String,
        isPending: Boolean
    )

    suspend fun getGroupList(): List<Group>
    suspend fun getMessageListByGroupId(groupId: String): List<ChatMessage>

    suspend fun deleteAllMessage(groupId: String)
    suspend fun deleteGroupWithMessage(groupId: String)


}