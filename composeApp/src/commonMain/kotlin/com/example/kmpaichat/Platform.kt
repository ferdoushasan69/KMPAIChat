package com.example.kmpaichat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform