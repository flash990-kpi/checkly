package com.example.checkly.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkly.domain.engine.AnalysisResult
import com.example.checkly.domain.engine.LocalSecurityEngine
import com.example.checkly.domain.ocr.MlKitOcrHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ChecklyUiState {
    object Input : ChecklyUiState()
    data class Analyzing(val step: Int, val description: String) : ChecklyUiState()
    data class Result(val result: AnalysisResult) : ChecklyUiState()
}

class ChecklyViewModel(
    private val securityEngine: LocalSecurityEngine,
    private val ocrHelper: MlKitOcrHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChecklyUiState>(ChecklyUiState.Input)
    val uiState: StateFlow<ChecklyUiState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.value = ChecklyUiState.Input
    }

    fun analyzeText(text: String) {
        if (text.isBlank()) return
        startAnalysis(text)
    }

    fun analyzeImage(uriOrPath: String) {
        viewModelScope.launch {
            _uiState.value = ChecklyUiState.Analyzing(1, "Estraggo il testo (ML Kit)...")
            val extractedText = ocrHelper.extractTextFromUri(uriOrPath)
            if (extractedText.isBlank()) {
                _uiState.value = ChecklyUiState.Result(
                    AnalysisResult(0, listOf("Non è stato rilevato alcun testo nell'immagine."), "Nessun Testo")
                )
                return@launch
            }
            startAnalysis(extractedText)
        }
    }

    private fun startAnalysis(text: String) {
        viewModelScope.launch {
            _uiState.value = ChecklyUiState.Analyzing(2, "Cerco link e informazioni...")
            delay(600) // Simulated delay for visual feedback

            _uiState.value = ChecklyUiState.Analyzing(3, "Riconosco i pattern di inganno...")
            delay(600)

            _uiState.value = ChecklyUiState.Analyzing(4, "Calcolo il rischio matematico locale...")
            delay(600)

            val localResult = securityEngine.analyze(text)

            _uiState.value = ChecklyUiState.Analyzing(5, "Verifica sul Cloud in corso (Fail-Safe)...")
            delay(800) // Simulated cloud call

            // In a real app, here we would call Gemini.
            // For now we use the local engine's result.
            _uiState.value = ChecklyUiState.Result(localResult)
        }
    }
}