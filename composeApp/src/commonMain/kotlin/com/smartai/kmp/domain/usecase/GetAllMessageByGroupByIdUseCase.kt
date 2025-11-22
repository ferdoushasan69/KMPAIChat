package com.smartai.kmp.domain.usecase

import com.smartai.kmp.domain.model.ChatMessage
import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.core.component.KoinComponent

class GetAllMessageByGroupByIdUseCase(
    private val repository: GeminiRepository
) : KoinComponent{
    suspend operator fun invoke(groupId: String) : List<ChatMessage>
    = repository.getMessageListByGroupId(groupId)
}