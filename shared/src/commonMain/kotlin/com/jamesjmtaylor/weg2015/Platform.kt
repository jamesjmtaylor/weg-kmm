package com.jamesjmtaylor.weg2015

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform