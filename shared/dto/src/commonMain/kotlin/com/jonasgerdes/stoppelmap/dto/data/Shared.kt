package com.jonasgerdes.stoppelmap.dto.data

import com.jonasgerdes.stoppelmap.dto.Localized
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: Double,
    val lng: Double,
)

typealias TagSlug = String

@Serializable
data class Tag(
    val slug: TagSlug,
    val name: Localized<String>,
    val aliases: List<Localized<String>>?
)


typealias Price = Int

typealias ServiceSlug = String

@Serializable
data class Service(
    val slug: ServiceSlug,
    val name: Localized<String>,
    val note: Localized<String>?,
    val aliases: List<Localized<String>>? = null,
    val phoneNumbers: List<PhoneNumber>? = null
)

typealias ProductSlug = String

@Serializable
data class Product(
    val slug: ProductSlug,
    val name: Localized<String>,
    val aliases: List<Localized<String>>? = null,
)

@Serializable
data class Offer(
    val productSlug: ProductSlug,
    val modifier: Localized<String>? = null,
    val price: Price? = null,
    val visible: Boolean = false,
)

@Serializable
data class Fee(
    val name: Localized<String>,
    val price: Price,
)

@Serializable
data class PhoneNumber(
    val number: String,
    val formatted: String,
)

@Serializable
data class Image(
    val url: String,
    val caption: Localized<String>? = null,
    val copyright: Localized<String>? = null,
    val blurHash: String,
    val preferredTheme: PreferredTheme? = null,
)

enum class PreferredTheme {
    Light,
    Dark
}


typealias OperatorSlug = String

@Serializable
data class Operator(
    val slug: OperatorSlug,
    val name: String,
    val websites: List<String> = emptyList(),
)