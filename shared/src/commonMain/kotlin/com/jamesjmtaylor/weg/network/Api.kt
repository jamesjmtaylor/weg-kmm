package com.jamesjmtaylor.weg.network

import com.jamesjmtaylor.weg.models.Contentlet
import com.jamesjmtaylor.weg.models.SearchResults
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class Api {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun getEquipmentSearchResults(category: String, page: Int, searchTerm: String? = null): List<Contentlet> {
        val query: String = searchTerm?.let {
            "+contentType:WegCard +categories:$category +(WegCard.name:(*$it*)^100)"
        } ?: "+contentType:WegCard +categories:$category"
        val results = httpClient.post<SearchResults?>(API_URL){
            contentType(ContentType.Application.Json)
            body = RequestBody(page * PAGE_SIZE, query)
        }?.entity?.jsonObjectView?.contentlets
        return results ?: emptyList()
    }

//    suspend fun getContentletById(equipmentId: Long): Contentlet {
//
//        val result = httpClient.post<ParseG2Response>(API_URL){
//            contentType(ContentType.Application.Json)
//        }.parseG2
//        result.images?.map { it.url = BASE_URL + it.url }
//        return result
//    }
    @Serializable
    data class RequestBody(val offset: Int, val query: String, val limit: Int = PAGE_SIZE, val sort: String = "score")

    companion object {
        const val BASE_URL = "https://odin.tradoc.army.mil/dotcms/"
        const val API_URL = "$BASE_URL/api/content/_search"
        const val PAGE_SIZE = 100
    }
}