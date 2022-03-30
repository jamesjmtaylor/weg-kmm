package com.jamesjmtaylor.weg

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}