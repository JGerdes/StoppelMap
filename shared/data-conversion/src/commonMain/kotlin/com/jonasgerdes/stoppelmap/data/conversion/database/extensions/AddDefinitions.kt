package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.map.Sub_type
import com.jonasgerdes.stoppelmap.data.schedule.Person
import com.jonasgerdes.stoppelmap.data.shared.Product
import com.jonasgerdes.stoppelmap.data.shared.Service
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData

internal fun StoppelMapDatabase.addDefinitions(data: StoppelMapData) {
    data.definitions.tags.forEach {
        tagQueries.insert(
            slug = it.slug,
            nameKey = addLocalizedString(it.name, it.slug, "name")
        )
        addAliases(it.slug, it.aliases)
    }

    data.definitions.subTypes.forEach {
        sub_typeQueries.insert(
            Sub_type(
                slug = it.slug,
                nameKey = addLocalizedString(it.name, it.slug, "name")
            )
        )
        addAliases(it.slug, it.aliases)
    }

    data.definitions.products.forEach {
        productQueries.insert(
            Product(
                slug = it.slug,
                nameKey = addLocalizedString(it.name, it.slug, "name")
            )
        )
        addAliases(it.slug, it.aliases)
    }

    data.definitions.services.forEach {
        serviceQueries.insert(
            Service(
                slug = it.slug,
                nameKey = addLocalizedString(it.name, it.slug, "name"),
                noteKey = addLocalizedString(it.name, it.slug, "note")
            )
        )
        addAliases(it.slug, it.aliases)
    }

    data.definitions.persons.forEach {
        personQueries.insert(
            Person(
                slug = it.slug,
                name = it.name,
                descriptionKey = it.description?.let { description ->
                    addLocalizedString(description, it.slug, "description")
                }
            )
        )
        addImages(it.slug, it.images)
        it.images.forEach { image ->
            person_imageQueries.insert(personSlug = it.slug, image = image.url)
        }
    }

    data.definitions.operators.forEach {
        operatorQueries.insert(
            slug = it.slug,
            name = it.name,
        )
        addWebsites(it.slug, it.websites)
    }
}