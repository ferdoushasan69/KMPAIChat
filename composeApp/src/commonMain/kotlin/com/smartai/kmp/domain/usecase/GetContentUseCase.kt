package com.smartai.kmp.domain.usecase

import com.smartai.kmp.domain.model.Gemini
import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.core.component.KoinComponent

class GetContentUseCase(private val repository: GeminiRepository) : KoinComponent {

    suspend operator fun invoke(content: String, apiKey: String, images: List<ByteArray>): Gemini =
        repository.generateContentWithImage(
            content = content,
            apiKey = apiKey,
            images = images
        )
}