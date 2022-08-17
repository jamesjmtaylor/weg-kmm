package com.jamesjmtaylor.weg.models

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json          = Json(JsonConfiguration.Stable)
// val searchResults = json.parse(SearchResults.serializer(), jsonString)

import kotlinx.serialization.*

@Serializable
data class SearchResults (
    val batchcomplete: String? = null,
    @SerialName("continue")
    val searchResultsContinue: Continue? = null,
    val query: Query? = null
) {
    fun asList(): List<SearchResult>? {
        return query?.searchResults
    }
}
@Serializable
data class Continue (
    val sroffset: Long? = null,
    @SerialName("continue")
    val continueString: String? = null
)

@Serializable
data class Query (
    @SerialName("searchG2")
    val searchResults: List<SearchResult> = emptyList(),
    @SerialName("totalhits")
    val totalHits: Long? = null
)

@Serializable
data class SearchResult (
    val title: String? = null,
    val id: Long = 0,
    val categories: List<String> = emptyList(),
    val images: List<Image> = emptyList()
)

@Serializable
data class Image (
    val name: String? = null,
    val url: String? = null
)


