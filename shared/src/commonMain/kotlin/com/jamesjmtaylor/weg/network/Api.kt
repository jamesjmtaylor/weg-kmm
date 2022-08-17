package com.jamesjmtaylor.weg.network

import com.jamesjmtaylor.weg.models.SearchResults
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class Api {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun getEquipment(): SearchResults {
        val equipmentURl = BASE_URL + "?format=json&" +
                "action=query&" +
                "list=searchG2&" +
                "srimages=1&" +
                "srsearch=incategory:Land&" +
                "srlimit=20" +
                "&sroffset=0"
        return httpClient.get(equipmentURl)
    }

    companion object {
        private const val BASE_URL = "https://odin.tradoc.army.mil/mediawiki/api.php"
    }
}