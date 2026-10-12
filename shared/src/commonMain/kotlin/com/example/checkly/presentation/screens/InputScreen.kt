package com.example.checkly.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkly.ui.theme.*

@Composable
fun InputScreen(
    onAnalyzeText: (String) -> Unit,
    onAnalyzeImage: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var textInput by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "🛡️ Checkly",
            color = SafeGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Il tuo scudo anti-truffa",
            color = TextWhite,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceDark)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabItem(
                text = "Screenshot", icon = Icons.Default.Image,
                isSelected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = "Link", icon = Icons.Default.Warning, // Changed Link to Warning as a fallback
                isSelected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = "Testo", icon = Icons.Default.Warning, // Changed Message to Warning as a fallback
                isSelected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (selectedImageUri == null) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    // Simulated Image Picker for Multiplatform
                                    selectedImageUri = "dummy_image_path"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SafeGreen)
                            ) {
                                Text("Seleziona Immagine", color = PureBlack)
                            }
                        } else {
                            Text("Immagine selezionata pronta per l'analisi.", color = SafeGreen)
                            Spacer(modifier = Modifier.height(16.dp))
                            TextButton(onClick = { selectedImageUri = null }) {
                                Text("Rimuovi", color = DangerRed)
                            }
                        }
                    }
                }
                1 -> {
                    Column {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Incolla un link qui...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SafeGreen,
                                unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                cursorColor = SafeGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                clipboardManager.getText()?.text?.let { textInput = it }
                            }
                        ) {
                            Text("Incolla dagli appunti", color = SafeGreen)
                        }
                    }
                }
                2 -> {
                    Column {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Incolla il testo del messaggio sospetto...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SafeGreen,
                                unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                cursorColor = SafeGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                clipboardManager.getText()?.text?.let { textInput = it }
                            }
                        ) {
                            Text("Incolla dagli appunti", color = SafeGreen)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedTab == 0 && selectedImageUri != null) {
                    onAnalyzeImage(selectedImageUri!!)
                } else if ((selectedTab == 1 || selectedTab == 2) && textInput.isNotBlank()) {
                    onAnalyzeText(textInput)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SafeGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = (selectedTab == 0 && selectedImageUri != null) || ((selectedTab == 1 || selectedTab == 2) && textInput.isNotBlank())
        ) {
            Text(
                text = "ANALIZZA ORA",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PureBlack
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Il testo degli screenshot viene estratto localmente.\nNessun dato personale viene memorizzato.",
            color = TextMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TabItem(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) BorderDark else SurfaceDark
    val contentColor = if (isSelected) SafeGreen else TextMuted

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}