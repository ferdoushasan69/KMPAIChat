package com.smartai.kmp.utils

import kotlinx.coroutines.CoroutineDispatcher

interface AppCoroutineDispatcher {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
}