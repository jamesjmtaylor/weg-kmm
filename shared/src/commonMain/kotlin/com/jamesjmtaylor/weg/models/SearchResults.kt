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
data class ParseG2Response (
    val parseG2: SearchResult
)

@Serializable
data class SearchResult (
    val title: String? = null,
    val id: Long,
    val categories: List<String> = emptyList(),
    val images: List<Image>? = null,
    @SerialName("json")
    val details: SearchResultDetails? = null,
    val page: Long? = null
)

@Serializable
data class SearchResultDetails (
    val tiers: List<Boolean>,
    val notes: String,
    val dateOfIntroduction: Long,
    val countryOfOrigin: String,
    val proliferation: String,
    val selectedregions: List<String>,
    val checkedcountries: List<String>,
    val sections: List<Section>,
    val variants: List<Variant>,
    val type: String,
    val version: Long
)

@Serializable
data class Section (
    val name: String,
    val properties: List<Property>
)

@Serializable
data class Property (
    val name: String,
    val value: String
)

@Serializable
data class Variant (
    val name: String,
    val notes: String
)

@Serializable
data class Image (
    val name: String? = null,
    var url: String? = null
)

@Serializable
data class PageProgress(
    val equipmentType: EquipmentType,
    val page: Long
)




