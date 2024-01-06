package com.jamesjmtaylor.weg

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform