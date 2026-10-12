package com.example.checkly.domain.ocr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import org.koin.java.KoinJavaComponent.getKoin

actual class MlKitOcrHelper actual constructor() {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    actual suspend fun extractTextFromUri(uriOrPath: String): String {
        return try {
            val context: Context = getKoin().get()
            val image = InputImage.fromFilePath(context, Uri.parse(uriOrPath))
            val result = recognizer.process(image).await()
            result.text
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}