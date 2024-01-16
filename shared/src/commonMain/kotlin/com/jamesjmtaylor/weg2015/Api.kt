package com.jamesjmtaylor.weg2015

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class Api {
    private val httpClient = HttpClient{
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HTTP Client: $message")
                }
            }
            level = LogLevel.ALL
        }
    }

    //TODO: io.ktor.serialization.JsonConvertException: Illegal input: Unexpected JSON token at offset 6024: Expected start of the object '{', but had ':' instead at path: $.entity.jsonObjectView.contentlets[0].domain
    suspend fun getSearchResults(category: String, page: Int, searchTerm: String? = null): SearchResults {
        val query: String = searchTerm?.let {
            "+contentType:WegCard +categories:$category +(WegCard.name:(*$it*)^100)"
        } ?: "+contentType:WegCard +categories:$category"
        val results = httpClient.post {
            url(API_URL)
            setBody(RequestBody(page * PAGE_SIZE, query))
        }.body<SearchResults>()
        return results
    }

    @Serializable
    data class RequestBody(val offset: Int, val query: String, val limit: Int = PAGE_SIZE, val sort: String = "score")

    companion object {
        const val BASE_URL = "https://odin.tradoc.army.mil/dotcms/"
        const val API_URL = "$BASE_URL/api/content/_search"
        const val PAGE_SIZE = 100
    }
}