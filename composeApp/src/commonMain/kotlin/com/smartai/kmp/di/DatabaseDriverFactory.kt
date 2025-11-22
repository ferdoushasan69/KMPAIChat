package com.smartai.kmp.di

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    suspend fun createDriver() : SqlDriver
}