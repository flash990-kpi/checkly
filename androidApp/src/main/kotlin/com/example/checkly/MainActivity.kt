package com.example.checkly

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.core.context.startKoin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Initialize Koin only once for the application
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(module {
                    single<Context> { this@MainActivity.applicationContext }
                })
            }
        }

        var sharedText: String? = null
        var sharedImageUri: String? = null

        if (intent?.action == Intent.ACTION_SEND) {
            when (intent.type) {
                "text/plain" -> {
                    sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                }
                else -> {
                    if (intent.type?.startsWith("image/") == true) {
                        @Suppress("DEPRECATION")
                        val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                        sharedImageUri = uri?.toString()
                    }
                }
            }
        }

        setContent {
            App(
                sharedText = sharedText,
                sharedImageUri = sharedImageUri
            )
        }
    }
}