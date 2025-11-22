package com.smartai.kmp.domain.usecase

import com.smartai.kmp.domain.model.Role
import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.core.component.KoinComponent

class InsertMessageUseCase(private val repository: GeminiRepository) : KoinComponent {
    suspend operator fun invoke(
        messageId: String,
        groupId: String,
        text: String,
        images: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    ) =
        repository.insertMessage(
            messageId = messageId,
            groupId = groupId,
            text = text,
            image = images,
            participant = participant,
            isPending = isPending
        )
}