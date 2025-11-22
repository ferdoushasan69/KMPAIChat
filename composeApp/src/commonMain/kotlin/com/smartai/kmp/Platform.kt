package com.smartai.kmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.ClipboardManager
import com.smartai.kmp.utils.AppCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect suspend fun clipData(clipboardManager: ClipboardManager): String?

expect suspend fun setClipData(clipboardManager: ClipboardManager, message: String)

@Composable
expect fun ImagePicker(
    showFilePicker: Boolean,
    onDismiss: () -> Unit,
    onResult: (ByteArray?) -> Unit
)

expect fun ByteArray.toComposeImageBitmap() : ImageBitmap

expect class AppCoroutineDispatcherImpl() : AppCoroutineDispatcher {
    override val io: CoroutineDispatcher
    override val default: CoroutineDispatcher
    override val main: CoroutineDispatcher
}

@Composable
expect fun TextComposable(message : String,isGeminiMessage : Boolean)

expect fun isNetworkAvailable() : Boolean