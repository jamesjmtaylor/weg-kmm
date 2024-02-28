package com.jamesjmtaylor.weg2015

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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
    @Serializable(with = Section.SectionSerializer::class)
    val sections: List<Section>,

    val domain: List<Map<String, String>>,
    val proliferation: List<Map<String, String>> = emptyList(),
    val origin: List<Map<String, String>> = emptyList(),
    val dateOfIntroduction: String? = null
)


private val json = Json{ ignoreUnknownKeys = true }
@Serializable
data class Image (
    val name: String? = null,
    var url: String? = null
) {
    object ImageSerializer : KSerializer<List<Image>> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("images", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: List<Image>) = encoder.encodeString(Json.encodeToString(value))
        override fun deserialize(decoder: Decoder): List<Image> {
            return  json.decodeFromString(decoder.decodeString())
        }
    }
}
@Serializable
data class Section (
    val name: String? = null,
    val value: String? = null,
    val units: String? = null,
    val sections: List<Section> = emptyList(),
    val properties: List<Property> = emptyList()
) {
    object SectionSerializer: KSerializer<List<Section>> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("sections", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: List<Section>) = encoder.encodeString(Json.encodeToString(value))
        override fun deserialize(decoder: Decoder): List<Section> {
            return  json.decodeFromString(decoder.decodeString())
        }
    }

    @Serializable
    data class Property(
        val name: String? = null,
        val value: String? = null,
        val units: String? = null
    )
}


/* TODO: Make Sections @Serializable that uses same pattern as Image above.  The reason is because of the wierd formatting below:
* "images": "[{\"name\":\"Russian7.62-mmGeneralPurposeMachinegunPKM.PNG\",\"url\":\"/dA/c7b153b8f1cb7577eb94b00ce160abad/fileAsset/Russian72cef.62mmGeneralPurposeMachinegunPKM\"},{\"name\":\"PKM_(B).jpg\",\"url\":\"/dA/70973069878fe3465c217bfc2a45d2b7/fileAsset/PKM_%28B%29c1b2.jpg\"},{\"name\":\"PKM_(C).jpg\",\"url\":\"/dA/172063aebcb63837e2ba5e75de2f016a/fileAsset/PKM_%28C%29a8f0.jpg\"}]",
* "sections": "[{\"name\":\"Variants\",\"properties\":[{\"name\":\"PKM\",\"value\":\"Squad machinegun .\"},{\"name\":\"PKT\",\"value\":\"Vehicle mounted MG with solenoid electric trigger, remote sight, and a longer heavier barrel. It lacks a stock and, bipod. Some are coaxial to a main gun and use its sights. Others operate separately. They generally do not dismount for ground use. \"},{\"name\":\"PKS\",\"value\":\"Lightweight tripod- mounted infantry weapon.\"},{\"name\":\"PKMS\",\"value\":\"Lightweight tripod- mounted variant of the PKS.\"},{\"name\":\"PKB (PKBM)\",\"value\":\"Pintle-mounted on APCs, SP guns, BRDM, BTRs, has butterfly trigger rather than solenoid, double spade grips, and front and rear sights.\"}]},{\"name\":\"System\",\"sections\":[{\"name\":\"Dimensions\",\"properties\":[{\"name\":\"Length, Overall \",\"value\":\"1,192 mm\"},{\"name\":\"Barrel Length\",\"value\":\"605 mm\"},{\"name\":\"Weight\",\"value\":\"7.5 kg\"}]}],\"properties\":[{\"name\":\"Alternative Designation \",\"value\":\"PKM\"},{\"name\":\"Type\",\"value\":\"General-Purpose Machine Gun\"},{\"name\":\"Caliber\",\"value\":\"7.62 mm\"},{\"name\":\"Feed System\",\"value\":\"Belt, 100-rd belt carried\\nin a box fastened to the right side of the receiver. 25-rd belts can be joined in several combination lengths (100/200/250)\\n\"},{\"name\":\"Crew \",\"value\":\"2.0\",\"units\":\"ea\"},{\"name\":\"Fire Mode \",\"value\":\"Automatic\"},{\"name\":\"Operation\",\"value\":\"Gas\"},{\"name\":\"Rate of Fire \",\"value\":\"Cyclic: 650 rd/m\"}]},{\"name\":\"Ammunition\",\"sections\":[{\"name\":\"Ammunition (Option 1)\",\"properties\":[{\"name\":\"Name\",\"value\":\"57-N-323S\"},{\"name\":\"Type\",\"value\":\"Ball\"},{\"name\":\"Caliber\",\"value\":\"7.62x54 (rimmed)\",\"units\":\"mm\"},{\"name\":\"Muzzle Velocity \",\"value\":\"825\",\"units\":\"m/s\"},{\"name\":\"Range, Maximum \",\"value\":\"3,800\",\"units\":\"m\"},{\"name\":\"Range, Practical, Day \",\"value\":\"1,000\",\"units\":\"m\"},{\"name\":\"Range, Practical, Night \",\"value\":\"300\",\"units\":\"m\"},{\"name\":\"Armor Penetration at 500m\",\"value\":\"6.0\",\"units\":\"mm\"}]},{\"name\":\"Ammunition (Option 2)\",\"properties\":[{\"name\":\"Name\",\"value\":\"7BZ-3\"},{\"name\":\"Type\",\"value\":\"API\"},{\"name\":\"Caliber\",\"value\":\"7.62x54mm (rimmed)\"},{\"name\":\"Muzzle Velocity \",\"value\":\"808.0\",\"units\":\"m/s\"},{\"name\":\"Range, Maximum \",\"value\":\"3,800\",\"units\":\"m\"},{\"name\":\"Range, Practical, Day \",\"value\":\"1000\",\"units\":\"m\"},{\"name\":\"Range, Practical, Night \",\"value\":\"300\",\"units\":\"m\"},{\"name\":\"Armor Penetration \",\"value\":\"10 mm at 200 range\"}]}]},{\"name\":\"Sights\",\"sections\":[{\"name\":\"Iron sights\",\"properties\":[{\"name\":\"Note\",\"value\":\"The rear sight assembly is riveted onto the receiver cover and consist of a square notched rear tangent iron sight calibrated in 100 m (109 yd) increments from 100 to 1,500 m (109 to 1,640 yd) and includes a \\\"point-blank range\\\" battle zero setting corresponding to a 330 m (361 yd) zero. It is identical in design to the AKM and Mosin\u2013Nagant, except that it is oriented backwards with the notch forward and the hinge behind. The iron sight line has a 663 mm (26.1 in) sight radius. Like the RPD rear sight, the PK rear sight also features full windage adjustment in the form of small dials on either side of the notch.\\n\\nThe front sight assembly is mounted near the end of the barrel and consists of a protected open post adjustable for elevation in the field.\"}]},{\"name\":\"Optical sights\",\"properties\":[{\"name\":\"Note\",\"value\":\"PK machine guns that feature a Warsaw Pact side-rail bracket on the left side of the receiver can mount various aiming optics. The standard Russian side rail mounted optical sight was the 4×26 1P29 Universal sight for small arms, an aiming optic similar to the British SUIT and SUSAT and Canadian C79 optical sights. When mounted, the 1P29 sight is positioned centered above the receiver at a height that allows the use of the iron sights. It weighs 0.8 kg, offers 4× magnification with a field of view of 8° and 35 mm eye relief. The 1P29 is issued with a canvas pouch, a lens cleaning cloth, combination tool, two rubber eyecups, two eyecup clamps and three different bullet drop compensation (BDC) cams for the AK-74, RPK-74 and PK machine gun. The 1P29 is intended for quickly engaging point and area targets at various ranges. On the right side of the field of view a stadiametric rangefinder is incorporated that can be used to determine the distance from a 1.5 meters (4 ft 11.1 in) tall object from 400 to 1,200 m (437 to 1,312 yd). The reticle is an inverted aiming post in the top half of the field of view and is tritium-illuminated for low-light condition aiming. A later designed similar optical sight suitable for the PK machine gun series is the 4×24 1Р77\"}]}]},{\"name\":\"Sources\",\"properties\":[{\"name\":\"Images\",\"units\":\"\",\"value\":\"https://www.1919a4.com/threads/marcolmar-semi-uk-vz-59s-now-available-4-295-shipping.45670/; https://www.amazon.com/Posterazzi-PSTACH100420M-Russian-general-purpose-machine/dp/B07H4M9G7V; https://www.imfdb.org/wiki/Talk:MAG#google_vignette\",\"propertyNameInvalid\":false,\"valueInvalid\":false}],\"nameInvalid\":false,\"sections\":[]}]",
* */