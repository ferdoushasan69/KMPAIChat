package com.smartai.kmp.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.smartai.kmp.database.AppDatabase
import com.smartai.kmp.utils.DB_NAME
import org.koin.mp.KoinPlatform

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            AppDatabase.Schema.synchronous(),
            KoinPlatform.getKoin().get(),
            DB_NAME
        )
    }
}