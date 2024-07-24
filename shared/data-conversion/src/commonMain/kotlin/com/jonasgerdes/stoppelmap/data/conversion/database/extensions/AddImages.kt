package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.dto.data.Image
import com.jonasgerdes.stoppelmap.dto.data.PreferredTheme

internal fun StoppelMapDatabase.addImages(referenceSlug: String, images: List<Image>) {
    images.forEachIndexed { index, image ->
        imageQueries.insert(
            com.jonasgerdes.stoppelmap.data.shared.Image(
                url = image.url,
                captionKey = image.caption?.let {
                    addLocalizedString(
                        it,
                        referenceSlug,
                        "image",
                        index.toString().padStart(2, '0'),
                        "caption"
                    )
                },
                copyrightKey = image.copyright?.let {
                    addLocalizedString(
                        it,
                        referenceSlug,
                        "image",
                        index.toString().padStart(2, '0'),
                        "copyright"
                    )
                },
                blurHash = image.blurHash,
                preferredTheme = when (image.preferredTheme) {
                    PreferredTheme.Light -> com.jonasgerdes.stoppelmap.data.shared.PreferredTheme.Light
                    PreferredTheme.Dark -> com.jonasgerdes.stoppelmap.data.shared.PreferredTheme.Dark
                    null -> null
                }
            )
        )
    }
}