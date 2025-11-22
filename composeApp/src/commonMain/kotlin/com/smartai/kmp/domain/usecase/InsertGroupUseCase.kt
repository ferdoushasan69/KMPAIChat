package com.smartai.kmp.domain.usecase

import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.core.component.KoinComponent

class InsertGroupUseCase(
    private val repository: GeminiRepository
) : KoinComponent {

    suspend operator fun invoke(groupId: String, title: String, date: String, icon: String) =
        repository.insertGroup(
            groupId = groupId,
            title = title,
            date = date,
            icon = icon
        )
}