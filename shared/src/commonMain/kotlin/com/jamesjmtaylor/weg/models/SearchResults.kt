package com.jamesjmtaylor.weg.models

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json          = Json(JsonConfiguration.Stable)
// val SearchResults = json.parse(SearchResults.serializer(), jsonString)

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

@Serializable
data class SearchResults (
    val entity: Entity,
    val errors: JsonArray,
    val messages: JsonArray,
    val permissions: JsonArray
)

@Serializable
data class Entity (
    val contentTook: Long,
    val jsonObjectView: JSONObjectView,
    val queryTook: Long,
    val resultsSize: Long
)

@Serializable
data class JSONObjectView (
    val contentlets: List<Contentlet>
)

@Serializable
data class Contentlet (
    val notes: String,
    val title: String,
    val identifier: String,
    @Serializable(with = Image.ImageSerializer::class)
    val images: List<Image>,

    val sections: String,
    val domain: Map<String, String>,
    val proliferation: Map<String, String>,
    val origin: Map<String, String>,
    val dateOfIntroduction: String? = null
)

@Serializable
data class Section (
    val name: String,
    val properties: List<Property>? = null,
    @SerialName("sections")
    val subsections: List<Subsection>? = null
)

@Serializable
data class Subsection (
    val name: String,
    val properties: List<Property>? = null
)

@Serializable
data class Property (
    val name: String,
    val value: String,
    val units: String? = null
)

@Serializable
data class Image (
    val name: String? = null,
    var url: String? = null
) {
    //TODO: Verify this works
    object ImageSerializer : KSerializer<List<Image>> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("images", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: List<Image>) = encoder.encodeString(Json.encodeToString(value))
        override fun deserialize(decoder: Decoder): List<Image> {
            return  Json.decodeFromString(decoder.decodeString())
        }
    }
}




