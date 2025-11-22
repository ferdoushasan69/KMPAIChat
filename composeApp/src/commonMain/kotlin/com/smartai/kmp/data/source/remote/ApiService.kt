package com.smartai.kmp.data.source.remote

import com.smartai.kmp.data.models.GeminiResponseDto

interface ApiService {

    suspend fun generateContent(content: String, apiKey: String): GeminiResponseDto

    suspend fun generateContentWithImage(
        content: String,
        apiKey: String,
        images: List<ByteArray> = emptyList()
    ): GeminiResponseDto
}