package com.example.checkly

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform