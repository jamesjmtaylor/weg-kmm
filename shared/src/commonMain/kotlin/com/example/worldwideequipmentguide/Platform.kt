package com.example.worldwideequipmentguide

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform