package com.jonasgerdes.stoppelmap.model.parse

import com.github.filosganga.geogson.gson.GeometryAdapterFactory
import com.github.filosganga.geogson.model.FeatureCollection
import com.github.filosganga.geogson.model.Polygon
import com.google.common.collect.ImmutableMap
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.jonasgerdes.stoppelmap.model.Data
import com.jonasgerdes.stoppelmap.model.createSlug
import com.jonasgerdes.stoppelmap.model.entity.*
import com.jonasgerdes.stoppelmap.model.splitBy
import com.jonasgerdes.stoppelmap.model.toShortHash
import java.io.File
import java.util.*

fun Data.parseGeoJson(input: File, output: File) {
    val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapterFactory(GeometryAdapterFactory())
            .create()
    val reader = JsonReader(input.reader())
    val geoJson = gson.fromJson<FeatureCollection>(reader, FeatureCollection::class.java)
    addTypeSubTypes(this)
    println("Read geojson")
    val updatedFeatures = geoJson.features()
            .map {
                if (it.properties()["building"] == null
                        || it.properties()["building"]!!.asString == "yes") {
                    it
                } else {
                    val center = it.geometry().let {
                        if (it is Polygon) {
                            it.coordinates().center()
                        } else {
                            null
                        }
                    }
                    val min = it.geometry().let {
                        (it as? Polygon)?.coordinates()?.min()
                    }
                    val max = it.geometry().let {
                        (it as? Polygon)?.coordinates()?.max()
                    }
                    val stall = Stall(slug = "placeholder",
                            type = it.properties()["building"]!!.asString,
                            name = it.properties()["name"]?.asString,
                            operator = it.properties()["operator"]?.asString,
                            priority = Integer.parseInt(it.properties()["priority"]?.asString
                                    ?: "0"),
                            description = it.properties()["description"]?.asString,
                            centerLng = center?.longitude,
                            centerLat = center?.latitude,
                            minLng = min?.longitude,
                            maxLng = max?.longitude,
                            minLat = min?.latitude,
                            maxLat = max?.latitude
                    ).run {
                        var slug = it.properties()["slug"]?.asString ?: createSlug()
                        if (stalls.map { it.slug }.contains(slug)) {
                            System.err.println("duplicate slug found: $slug")
                            slug += "-" + UUID.randomUUID().toString().toShortHash()
                        }
                        copy(slug = slug)
                    }
                    stalls += stall

                    images += it.properties()["pictures"].splitBy(";", ":") {
                        Image(stall = stall.slug,
                                file = it[0],
                                type = it[1],
                                author = it.getOrNull(2),
                                license = it.getOrNull(3))
                    }

                    it.properties()["alias"]?.let {
                        alias += it.asString.split(";").map {
                            Alias(stall = stall.slug, alias = it)
                        }
                    }

                    it.properties()["website"]?.let {
                        urls += Url(stall.slug, it.asString, "website")
                    }

                    phones += it.properties()["phone"].splitBy(";", ":") {
                        Phone(stall = stall.slug,
                                name = it[0],
                                number = it[1],
                                numberReadable = it[2])
                    }

                    if (stall.type == "restroom") {
                        restrooms += Restroom(
                                stall = stall.slug,
                                accessible = it.properties()["accessible"]?.asString ?: "no" == "yes",
                                forWomen = it.properties()["women"]?.asString ?: "no" == "yes",
                                forMen = it.properties()["men"]?.asString ?: "no" == "yes"
                        )
                    }

                    it.properties().filter {
                        it.key.startsWith("item_")
                                || it.key.startsWith("game_")
                    }
                            .forEach {
                                val names = getNamesForItem(it.key)
                                if (!items.containsKey(it.key)) {
                                    items[it.key] = names.map { name ->
                                        Item(slug = it.key.removePrefix("item_")
                                                .removePrefix("game_"),
                                                name = name
                                        )
                                    }
                                }
                                stallItems += StallItem(
                                        stall = stall.slug,
                                        item = items[it.key]!!.first().slug
                                )
                            }

                    it.properties()["type"]?.asString?.let { subType ->
                        saveSubType(subType, stall)
                    }

                    //todo add special subtypes like Festzelt, Ausschank etc
                    if (stall.type == "bar") {
                        if (it.properties()["isTent"]?.asString ?: "no" == "yes") {
                            saveSubType("marquee", stall)
                        } else {
                            saveSubType("bar", stall)
                        }
                    }
                    if (it.properties()["forKids"]?.asString ?: "no" == "yes") {
                        saveSubType("for-kids", stall)
                    }
                    if (stall.type == "restroom") {
                        saveSubType("restroom", stall)
                        if (it.properties()["accessible"]?.asString ?: "no" == "yes") {
                            saveSubType("accessible_restroom", stall)
                        }
                        if (it.properties()["women"]?.asString ?: "no" == "yes") {
                            saveSubType("womens_restroom", stall)
                        }
                        if (it.properties()["men"]?.asString ?: "no" == "yes") {
                            saveSubType("mens_restroom", stall)
                        }
                    }

                    it.let {
                        if (it.properties().containsKey("slug")) {
                            it
                        } else {
                            it.withProperty("slug", JsonObject().let {
                                it.addProperty("slug", stall.slug)
                                it.get("slug")
                            })
                        }
                    }.let {
                        val priority = it.properties()["priority"]?.asString?.toInt() ?: 1
                        val props = it.properties().toMutableMap()
                        props.put("priority", JsonObject().let {
                            it.addProperty("priority", priority)
                            it.get("priority")
                        })
                        it.withProperties(ImmutableMap.copyOf(props))
                    }
                }

            }

    val jsonWriter = JsonWriter(output.writer())
    gson.toJson(FeatureCollection(updatedFeatures), FeatureCollection::class.java, jsonWriter)
    jsonWriter.close()
    println("Created ${stalls.size} stalls from geojson")

}

fun addTypeSubTypes(data: Data) {
    listOf("bar",
            "building",
            "candy-stall",
            "exhibition",
            "food-stall",
            "game-stall",
            "misc",
            "restroom",
            "ride",
            "seller-stall").forEach { data.addSubType(it) }
}

private fun Data.saveSubType(subType: String, stall: Stall) {
    if (subType != "flat") {
        addSubType(subType)
        stallSubTypes += StallSubType(stall = stall.slug, subType = subType)
    }
}

private fun Data.addSubType(subType: String) {
    if (subTypes.none { it.slug == subType }) {
        subTypes += getNamesForType(subType)
                .map { SubType(subType, it) }
    }
}