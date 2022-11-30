package com.jamesjmtaylor.weg.models

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json          = Json(JsonConfiguration.Stable)
// val searchResults = json.parse(SearchResults.serializer(), jsonString)

import com.jamesjmtaylor.weg.EquipmentType
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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
    val tiers: List<Boolean> = emptyList(),
    val notes: String? = null,
    @Serializable(with = DateOfIntroduction.DateOfIntroductionLongSerializer::class)
    val dateOfIntroduction: DateOfIntroduction? = null,
    val countryOfOrigin: String? = null,
    val proliferation: String? = null,
    val selectedRegions: List<String> = emptyList(),
    val checkedCountries: List<String> = emptyList(),
    val sections: List<Section> = emptyList(),
    val variants: List<Variant> = emptyList(),
    val type: String? = null,
    val version: Long
)

/**
 * Handles mixed ODIN API type that can either be a long (i.e. 1984) or a string (i.e. "INA").
 * @property value the underlying value of the date of introduction.
 * @property description the value used to describe the date of introduction.  Either a full date
 * (in string format) or "INA" for "Is Not Available"
 */
@Serializable
data class DateOfIntroduction(private val value: Long? = null, val description: String){
    object DateOfIntroductionLongSerializer : KSerializer<DateOfIntroduction> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("dateOfIntroduction", PrimitiveKind.LONG)
        override fun serialize(encoder: Encoder, value: DateOfIntroduction) = encoder.encodeLong(value.value ?: 0)
        override fun deserialize(decoder: Decoder): DateOfIntroduction {
            return try {
                val long = decoder.decodeLong()
                DateOfIntroduction(long, long.toString())
            } catch (e: Exception) {
                val string = decoder.decodeString()
                DateOfIntroduction(description = string)
            }
        }
    }
}



@Serializable
data class Section (
    val name: String,
    val properties: List<Property>? = null,
    val sections: List<Subsection>? = null
)

@Serializable
data class Subsection (
    val name: String,
    val properties: List<Property>
)


@Serializable
data class Property (
    val name: String,
    val value: String,
    val units: String? = null
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




