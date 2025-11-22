package com.smartai.kmp.di

import org.koin.dsl.module

val AppModule = module {
    includes(
        LocalModule,
        ApiServiceModule,
        NetworkModule,
        RepositoryModule,
        UseCaseModule,
        ViewModelModule
    )
}