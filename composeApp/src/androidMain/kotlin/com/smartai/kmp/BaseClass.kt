package com.smartai.kmp

import android.app.Application
import com.smartai.kmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class BaseClass : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BaseClass)
        }
    }
}