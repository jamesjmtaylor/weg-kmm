package com.jamesjmtaylor.weg.models

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json          = Json(JsonConfiguration.Stable)
// val searchResults = json.parse(SearchResults.serializer(), jsonString)

import com.jamesjmtaylor.weg.EquipmentType
import kotlinx.serialization.*

@Serializable
data class SearchResults (
    val batchcomplete: String? = null,
    /**
     * This field is null if you paginate beyond the total hits
     */
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
@SerialName("parseG2")
data class ParseG2 (
    val searchResult: SearchResult? = null
)

@Serializable
data class SearchResult (
    val title: String? = null,
    val id: Long = 0,
    val categories: List<String> = emptyList(),
    val images: List<Image> = emptyList(),
    val json: SearchResultDetails? = null
)

@Serializable
data class SearchResultDetails (
    val tiers: List<Boolean> = emptyList(),
    val notes: String? = null,
    val dateOfIntroduction: Long? = null,
    val countryOfOrigin: String? = null,
    val proliferation: String? = null,
    val sections: List<DetailsSections> = emptyList()
)

@Serializable
data class DetailsSections (
    val name: String? = null,
    val properties: List<SectionProperties> = emptyList()
)

@Serializable
data class SectionProperties (
    val name: String? = null,
    val value: String? = null
)

@Serializable
data class Image (
    val name: String? = null,
    val url: String? = null
)

@Serializable
data class PageProgress(
    val equipmentType: EquipmentType,
    val page: Long
)




