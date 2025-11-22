package com.smartai.kmp.di

import com.smartai.kmp.data.source.repository.GeminiRepositoryImpl
import com.smartai.kmp.domain.repository.GeminiRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single<GeminiRepository> { GeminiRepositoryImpl(get(), get()) }
}