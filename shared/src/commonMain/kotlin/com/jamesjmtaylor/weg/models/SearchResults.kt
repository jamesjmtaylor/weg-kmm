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

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

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
    val hostName: String,
    val modDate: String,
    val notes: String,
    val disstring: String,
    val publishDate: String,
    val title: String,
    val baseType: String,
    val inode: String,
    val archived: Boolean,
    val host: String,
    val working: Boolean,
    val locked: Boolean,
    val stInode: String,
    val contentType: String,
    val live: Boolean,
    val filterlabel: String,
    val owner: String,
    val identifier: String,
    val images: String,

    @SerialName("languageId")
    val languageID: Long,

    val sections: String,
    val url: String,
    val titleImage: String,
    val modUserName: String,
    val hasLiveVersion: Boolean,
    val folder: String,
    val hasTitleImage: Boolean,
    val sortOrder: Long,
    val modUser: String,
    val name: String,
    val disname: String,

    @SerialName("__icon__")
    val icon: String,

    val contentTypeIcon: String,
    val dateOfIntroduction: String? = null
)
/**
 * Handles mixed ODIN API type that can either be a long (i.e. 1984) or a string (i.e. "INA").
 * @property value the underlying value of the date of introduction as retrieved from the API.
 * @property description the value used to describe the date of introduction.  Either a full date
 * (in string format) or "INA" for "Is Not Available"
 */
@Serializable
data class DateOfIntroduction(private val value: Long? = null, val description: String){
    object DateOfIntroductionSerializer : KSerializer<DateOfIntroduction> {
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
)





