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

    suspend fun getEquipmentSearchResults(category: String, page: Int): SearchResults {
        val equipmentURl = API_URL + "?format=json&" +
                "action=query&" +
                "list=searchG2&" +
                "srimages=1&" +
                "srsearch=incategory:$category&" +
                "srlimit=${PAGE_SIZE}" + //How many results to fetch
                "&sroffset=${page * PAGE_SIZE}" //Where to start fetching results
        return httpClient.get(equipmentURl)
    }

    companion object {
        const val BASE_URL = "https://odin.tradoc.army.mil"
        const val API_URL = "$BASE_URL/mediawiki/api.php"
        const val PAGE_SIZE = 50
    }
}