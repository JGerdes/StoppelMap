package com.jonasgerdes.stoppelmap.preperation

import com.google.gson.JsonElement
import com.jonasgerdes.stoppelmap.preperation.entity.Stall
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

fun String.asMd5(): String {
    val digest = MessageDigest.getInstance("MD5").apply {
        reset()
        update(toByteArray())
    }.digest()
    val bigInt = BigInteger(1, digest)
    return bigInt.toString(16)
}

fun String.toShortHash(): String {
    return Base64.getEncoder().encodeToString(asMd5().toByteArray()).substring(0, 6)
}

fun String.asSlug(): String {
    var clean = replace("ß".toRegex(), "ss")
            .replace("ä".toRegex(), "ae")
            .replace("ö".toRegex(), "oe")
            .replace("ü".toRegex(), "ue")
            .replace("Ä".toRegex(), "Ae")
            .replace("Ö".toRegex(), "Oe")
            .replace("Ü".toRegex(), "Ue")
            .replace("/".toRegex(), "-")
            .replace("&".toRegex(), "-")
            .replace(",".toRegex(), "-")
            .replace("\\.".toRegex(), "-")
            .replace("\"".toRegex(), "")
            .replace(" ".toRegex(), "-")
            .replace("'".toRegex(), "-")
            .replace("_".toRegex(), "-")
    do {
        clean = clean.replace("--".toRegex(), "-")
    } while (clean.contains("--"))
    return clean.toLowerCase()
}


fun <T> JsonElement?.splitBy(primaryDelimiter: String, secondaryDelimiter: String,
                             action: (List<String>) -> T): List<T> {
    return if (this != null) {
        asString.split(primaryDelimiter).map {
            action(it.split(secondaryDelimiter))
        }
    } else {
        emptyList()
    }
}


fun Stall.createSlug(): String {
    var slug = ""
    var addHash = true

    if (name != null) {
        slug = name + "-"
        addHash = true
    }

    if (operator != null) {
        if (name == null || !name.contains(operator)) {
            slug += operator + "-"
        }
        if (name != null) {
            addHash = false
        }
    }

    if (operator == null && name == null) {
        slug = type + "-"
        addHash = true
    }

    if (addHash) {
        val id = "$centerLat $centerLng $minLat $minLng $maxLat $maxLng"
        slug += id.toShortHash()
    }

    return slug.removeSuffix("-").asSlug()
}