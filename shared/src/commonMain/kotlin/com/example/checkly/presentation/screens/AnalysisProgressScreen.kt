package com.example.checkly.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkly.ui.theme.PureBlack
import com.example.checkly.ui.theme.SafeGreen
import com.example.checkly.ui.theme.TextMuted
import com.example.checkly.ui.theme.TextWhite

@Composable
fun AnalysisProgressScreen(
    currentStep: Int,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(120.dp),
                color = SafeGreen,
                strokeWidth = 6.dp
            )
            Text(
                text = "$currentStep/5",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Analisi in corso...",
            color = SafeGreen,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = description,
            color = TextMuted,
            fontSize = 16.sp
        )
    }
}