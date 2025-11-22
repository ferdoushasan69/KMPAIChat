package com.smartai.kmp.domain.usecase

import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.core.component.KoinComponent

class UpdatePendingUseCase(
    private val repository: GeminiRepository
) : KoinComponent {

    suspend operator fun invoke(
        messageId: String,
        isPending: Boolean
    ) =
        repository.updatePendingStatus(
            messageId = messageId,
            isPending = isPending
        )
}