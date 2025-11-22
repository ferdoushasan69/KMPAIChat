package com.smartai.kmp.di

import com.smartai.kmp.domain.usecase.DeleteGroupWithMessageUseCase
import com.smartai.kmp.domain.usecase.DeleteMessageUseCase
import com.smartai.kmp.domain.usecase.GetAllGroupUseCase
import com.smartai.kmp.domain.usecase.GetAllMessageByGroupByIdUseCase
import com.smartai.kmp.domain.usecase.GetContentUseCase
import com.smartai.kmp.domain.usecase.InsertGroupUseCase
import com.smartai.kmp.domain.usecase.InsertMessageUseCase
import com.smartai.kmp.domain.usecase.UpdatePendingUseCase
import org.koin.dsl.module

val UseCaseModule = module {
    factory { GetAllGroupUseCase(get()) }
    factory { GetAllMessageByGroupByIdUseCase(get()) }
    factory { GetContentUseCase(get()) }
    factory { InsertMessageUseCase(get()) }
    factory { InsertGroupUseCase(get()) }
    factory { UpdatePendingUseCase(get()) }
    factory { DeleteMessageUseCase(get()) }
    factory { DeleteGroupWithMessageUseCase(get()) }
}