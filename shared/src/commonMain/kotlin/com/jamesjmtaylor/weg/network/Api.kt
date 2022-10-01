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

    suspend fun getEquipment(category: String, page: Int): SearchResults {
        val equipmentURl = API_URL + "?format=json&" +
                "action=query&" +
                "list=searchG2&" +
                "srimages=1&" +
                "srsearch=incategory:$category&" +
                "srlimit=${(page + 1) * 50}" +
                "&sroffset=${(page) * 50}"
        return httpClient.get(equipmentURl)
    }

    companion object {
        const val BASE_URL = "https://odin.tradoc.army.mil"
        const val API_URL = "$BASE_URL/mediawiki/api.php"
    }
}