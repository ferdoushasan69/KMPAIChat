package com.smartai.kmp.domain.usecase

import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.core.component.KoinComponent

class DeleteGroupWithMessageUseCase(
    private val repository: GeminiRepository
) : KoinComponent {

    suspend operator fun invoke(groupId: String) =
        repository.deleteGroupWithMessage(groupId)
}