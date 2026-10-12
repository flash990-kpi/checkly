package com.example.checkly

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.checkly.domain.engine.LocalSecurityEngine
import com.example.checkly.domain.ocr.MlKitOcrHelper
import com.example.checkly.presentation.ChecklyUiState
import com.example.checkly.presentation.ChecklyViewModel
import com.example.checkly.presentation.screens.AnalysisProgressScreen
import com.example.checkly.presentation.screens.InputScreen
import com.example.checkly.presentation.screens.ResultScreen
import com.example.checkly.ui.theme.ChecklyTheme

@Composable
fun App(
    sharedText: String? = null,
    sharedImageUri: String? = null
) {
    ChecklyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChecklyAppNavHost(sharedText, sharedImageUri)
        }
    }
}

@Composable
fun ChecklyAppNavHost(
    sharedText: String?,
    sharedImageUri: String?
) {
    // In a real KMP app we would use DI like Koin for this. 
    // Here we instantiate it manually for simplicity to keep it compilable without Koin KMP ViewModel boilerplate.
    val viewModel = remember { 
        ChecklyViewModel(
            securityEngine = LocalSecurityEngine(),
            ocrHelper = MlKitOcrHelper()
        ) 
    }
    
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(sharedText, sharedImageUri) {
        if (sharedText != null) {
            viewModel.analyzeText(sharedText)
        } else if (sharedImageUri != null) {
            viewModel.analyzeImage(sharedImageUri)
        }
    }

    when (val state = uiState) {
        is ChecklyUiState.Input -> {
            InputScreen(
                onAnalyzeText = { text -> viewModel.analyzeText(text) },
                onAnalyzeImage = { uri -> viewModel.analyzeImage(uri) }
            )
        }
        is ChecklyUiState.Analyzing -> {
            AnalysisProgressScreen(
                currentStep = state.step,
                description = state.description
            )
        }
        is ChecklyUiState.Result -> {
            ResultScreen(
                result = state.result,
                onReset = { viewModel.reset() }
            )
        }
    }
}
