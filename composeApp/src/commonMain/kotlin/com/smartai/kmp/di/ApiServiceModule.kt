package com.smartai.kmp.di

import com.smartai.kmp.data.source.remote.ApiService
import com.smartai.kmp.data.source.remote.ApiServiceImpl
import org.koin.dsl.module

val ApiServiceModule = module {
    single<ApiService>{ ApiServiceImpl(get()) }
}