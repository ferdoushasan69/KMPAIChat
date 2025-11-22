package com.smartai.kmp.domain.usecase

import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.core.component.KoinComponent

class GetAllGroupUseCase(
    private val repository: GeminiRepository
) : KoinComponent {

    suspend operator fun invoke() = repository.getGroupList()
}