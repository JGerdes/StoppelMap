package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.map.Sub_type
import com.jonasgerdes.stoppelmap.data.schedule.Person
import com.jonasgerdes.stoppelmap.data.shared.Product
import com.jonasgerdes.stoppelmap.data.shared.Service
import com.jonasgerdes.stoppelmap.dto.data.Definitions

internal fun StoppelMapDatabase.addDefinitions(definitions: Definitions) = with(definitions) {
    tags.forEach { tag ->
        tagQueries.insert(
            slug = tag.slug,
            nameKey = addLocalizedString(tag.name, tag.slug, "name"),
            isSearchable = tag.isSearchable,
        )
        addAliases(tag.slug, tag.aliases)
        tag.associatedTypes.forEach {
            tag_associated_typeQueries.insertTypeAssociation(tag.slug, it.toMapEntityType())
        }
        tag.associatedSubTypes.forEach {
            tag_associated_typeQueries.insertSubTypeAssociation(tag.slug, it)
        }
    }

    subTypes.forEach {
        sub_typeQueries.insert(
            Sub_type(
                slug = it.slug,
                nameKey = addLocalizedString(it.name, it.slug, "name")
            )
        )
        addAliases(it.slug, it.aliases)
    }

    products.forEach {
        productQueries.insert(
            Product(
                slug = it.slug,
                nameKey = addLocalizedString(it.name, it.slug, "name")
            )
        )
        addAliases(it.slug, it.aliases)
    }

    services.forEach { service ->
        serviceQueries.insert(
            Service(
                slug = service.slug,
                nameKey = addLocalizedString(service.name, service.slug, "name"),
                noteKey = service.note?.let { addLocalizedString(it, service.slug, "note") }
            )
        )
        addAliases(service.slug, service.aliases)
    }

    persons.forEach {
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

    operators.forEach {
        operatorQueries.insert(
            slug = it.slug,
            name = it.name,
        )
        addWebsites(it.slug, it.websites)
    }
}