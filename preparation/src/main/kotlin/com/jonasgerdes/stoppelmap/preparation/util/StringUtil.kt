package com.jonasgerdes.stoppelmap.preperation

import com.google.gson.JsonElement
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Base64

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
        .replace("/".toRegex(), "_")
        .replace("&".toRegex(), "_")
        .replace(",".toRegex(), "_")
        .replace("\\.".toRegex(), "_")
        .replace("\"".toRegex(), "")
        .replace(" ".toRegex(), "_")
        .replace("'".toRegex(), "_")
        .replace("-".toRegex(), "_")
    do {
        clean = clean.replace("__".toRegex(), "_")
    } while (clean.contains("__"))
    return clean.lowercase()
}


fun <T> JsonElement?.splitBy(
    primaryDelimiter: String, secondaryDelimiter: String,
    action: (List<String>) -> T
): List<T> {
    return if (this != null) {
        asString.split(primaryDelimiter).map {
            action(it.split(secondaryDelimiter))
        }
    } else {
        emptyList()
    }
}

fun <T> String.splitBy(
    primaryDelimiter: String,
    secondaryDelimiter: String,
    action: (List<String>) -> T
): List<T> =
    split(primaryDelimiter).map {
        action(it.split(secondaryDelimiter))
    }

fun String.splitSafe(delimiter: String, expectedSize: Int) =
    split(delimiter).padWithNulls(expectedSize)

private fun List<String>.padWithNulls(length: Int): List<String?> {
    return this + arrayOfNulls<String>(length - size).toList()
}