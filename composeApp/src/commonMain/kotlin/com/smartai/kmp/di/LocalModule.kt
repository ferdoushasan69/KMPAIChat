package com.smartai.kmp.di

import app.cash.sqldelight.db.SqlDriver
import com.smartai.kmp.data.db.SharedDatabase
import org.koin.dsl.module

val LocalModule = module {
    single { get<DatabaseDriverFactory>() }
    single { SharedDatabase(get()) }
}