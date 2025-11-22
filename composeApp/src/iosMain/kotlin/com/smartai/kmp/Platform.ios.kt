package com.smartai.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.smartai.kmp.presentation.component.CommonTextComposable
import com.smartai.kmp.utils.AppCoroutineDispatcher
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.io.bytestring.toByteString
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image
import org.jetbrains.skia.makeFromEncoded
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSURL
import platform.Foundation.NSURLConnection
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLResponse
import platform.Foundation.getBytes
import platform.Foundation.sendSynchronousRequest
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.presentationController
import platform.darwin.NSObject
import platform.zlib.alloc_func

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual suspend fun setClipData(
    clipboardManager: ClipboardManager,
    message: String
) = clipboardManager.setText(AnnotatedString(message))

@Composable
actual fun ImagePicker(
    showFilePicker: Boolean,
    onDismiss: () -> Unit,
    onResult: (ByteArray?) -> Unit
) {

    if (!showFilePicker) return
    DisposableEffect(Unit) {
        val picker = UIImagePickerController().apply {
            sourceType =
                UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            allowsEditing = false
            delegate = object : NSObject(), UIImagePickerControllerDelegateProtocol,
                UINavigationControllerDelegateProtocol {
                override fun imagePickerController(
                    picker: UIImagePickerController,
                    didFinishPickingMediaWithInfo: Map<Any?, *>
                ) {
                    val image =
                        didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
                    val data = image?.let {
                        UIImagePNGRepresentation(it) ?: UIImageJPEGRepresentation(
                            it,
                            0.9
                        )
                    }

                    //convert NSData to byArray
                    val byteArray = data?.toByteArray()
                    picker.dismissViewControllerAnimated(true) {
                        onResult(byteArray)
                        onDismiss()
                    }
                }

                override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                    picker.dismissViewControllerAnimated(true) {
                        onResult(null)
                        onDismiss()
                    }
                }

            }
        }

        val root = UIApplication.sharedApplication.keyWindow?.rootViewController
        root?.presentViewController(picker, animated = true, completion = null)
        onDispose { }
    }

}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

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
    CommonTextComposable(isGeminiMessage = isGeminiMessage, message = message)
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun isNetworkAvailable(): Boolean {
    val url = NSURL.URLWithString("https://www.google.com")
    val request = url?.let { NSURLRequest.requestWithURL(it) }

    val responsePtr = nativeHeap.alloc<ObjCObjectVar<NSURLResponse?>>().ptr
    val errorPtr = nativeHeap.alloc<ObjCObjectVar<NSError?>>().ptr

    val data = request?.let { NSURLConnection.sendSynchronousRequest(it, responsePtr, errorPtr) }
    val response = responsePtr.pointed.value
    val error = errorPtr.pointed.value

    return when {
        data != null && response is NSHTTPURLResponse -> true
        error != null -> false
        else -> false
    }
}

actual suspend fun clipData(clipboardManager: ClipboardManager): String? =
    clipboardManager.getText()?.text.toString().trim()


@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val size = this.length.toInt()
    val byteArray = ByteArray(size)

    byteArray.usePinned { pinned ->
        this.getBytes(pinned.addressOf(0), this.length)
    }

    return byteArray
}