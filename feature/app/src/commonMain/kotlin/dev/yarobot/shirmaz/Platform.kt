package dev.yarobot.shirmaz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform