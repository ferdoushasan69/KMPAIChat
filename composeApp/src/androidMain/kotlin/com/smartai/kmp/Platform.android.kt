package com.smartai.kmp

import android.Manifest
import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.smartai.kmp.presentation.component.CommonTextComposable
import com.smartai.kmp.utils.AppCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.mp.KoinPlatform

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun ImagePicker(
    showFilePicker: Boolean,
    onDismiss: () -> Unit,
    onResult: (ByteArray?) -> Unit
) {
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imgUri ->
        imgUri?.let {
            onResult(context.contentResolver.openInputStream(it)?.readBytes())
        }
        onDismiss()
    }
    if (showFilePicker) {
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap =
    BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()

actual class AppCoroutineDispatcherImpl actual constructor() :
    AppCoroutineDispatcher {
    actual override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    actual override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    actual override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}

@Composable
actual fun TextComposable(message: String, isGeminiMessage: Boolean) {
    CommonTextComposable(isGeminiMessage, message)
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
actual fun isNetworkAvailable(): Boolean {
    val context: Context = KoinPlatform.getKoin().get()
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val cap = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

actual suspend fun setClipData(
    clipboardManager: ClipboardManager,
    message: String
) = clipboardManager.setText(AnnotatedString(message))

actual suspend fun clipData(clipboardManager: ClipboardManager): String? =
    clipboardManager.getText()?.text.toString().trim()
