package com.smartai.kmp.di

import com.smartai.kmp.AppCoroutineDispatcherImpl
import com.smartai.kmp.presentation.screen.chat.ChatViewModel
import com.smartai.kmp.presentation.screen.main_screen.MainViewModel
import com.smartai.kmp.utils.AppCoroutineDispatcher
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val ViewModelModule = module {
    single<AppCoroutineDispatcher> { AppCoroutineDispatcherImpl() }
    factoryOf(::MainViewModel)
    factoryOf(::ChatViewModel)
}