package com.example.checkly.domain.ocr

expect class MlKitOcrHelper() {
    suspend fun extractTextFromUri(uriOrPath: String): String
}