package com.example.checkly.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkly.domain.engine.AnalysisResult
import com.example.checkly.ui.theme.*
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ResultScreen(
    result: AnalysisResult,
    onReset: () -> Unit
) {
    val (color, icon, title, actionText) = when {
        result.score < 31 -> listOf(SafeGreen, Icons.Default.CheckCircle, "Sembra Sicuro", "Nessuna minaccia rilevata. Puoi procedere con cautela.")
        result.score < 70 -> listOf(WarningAmber, Icons.Default.Warning, "Sospetto", "Presta attenzione. Contiene elementi ambigui.")
        else -> listOf(DangerRed, Icons.Default.Warning, "Pericolo", "Non cliccare su alcun link. Potrebbe essere una truffa.") // Replaced Error with Warning since Error might not be in common multiplatform icons
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Tachimetro simulato
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(80.dp))
                .border(4.dp, color as androidx.compose.ui.graphics.Color, RoundedCornerShape(80.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${result.score}",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = "/ 100",
                    fontSize = 16.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon as ImageVector,
                contentDescription = null,
                tint = color as androidx.compose.ui.graphics.Color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title as String,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Text(
            text = result.type,
            fontSize = 18.sp,
            color = TextWhite,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Cosa abbiamo notato",
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            result.reasons.forEach { reason ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("•", color = color as androidx.compose.ui.graphics.Color, modifier = Modifier.padding(end = 8.dp))
                    Text(text = reason, color = TextGray, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Cosa devi fare adesso",
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = actionText as String,
                color = TextWhite,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Fai un'altra verifica", color = TextWhite, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}