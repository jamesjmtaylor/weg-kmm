package com.jamesjmtaylor.weg.network

import com.jamesjmtaylor.weg.models.ParseG2Response
import com.jamesjmtaylor.weg.models.SearchResult
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

    suspend fun getEquipmentSearchResults(category: String, page: Int, searchTerm: String? = null): SearchResults {
        val srSearch = if (searchTerm != null)  //Filter search to category & searchTerm
            "srsearch=intitle:$searchTerm+incategory:$category&"
        else  //Filter search to just the provided category
            "srsearch=incategory:$category&"
        val searchURl = API_URL + "?format=json&" +
                "action=query&" + //Query, allowing search parameters
                "list=searchG2&" + //Fetch from the G2 database
                "srimages=1&" + //Fetch images with results
                srSearch +
                "srlimit=${PAGE_SIZE}" + //How many results to fetch
                "&sroffset=${page * PAGE_SIZE}" //Where to start fetching results
        return httpClient.get(searchURl)
    }

    //TODO: Persist the result to the database using the various tables in the [Database] class.
    suspend fun getEquipmentById(equipmentId: Int): SearchResult {
        val equipmentUrl = API_URL + "?format=json&" +
            "action=parseG2&" + //parseG2, returning equipment details
            "formatversion=2&" + //version 2 is the latest API json result format
            "pageid=$equipmentId" //the equipment to retrieve
        return httpClient.get<ParseG2Response>(equipmentUrl).parseG2
    }

    companion object {
        const val BASE_URL = "https://odin.tradoc.army.mil"
        const val API_URL = "$BASE_URL/mediawiki/api.php"
        const val PAGE_SIZE = 10
    }
}