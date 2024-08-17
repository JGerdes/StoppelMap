package com.jonasgerdes.stoppelmap.preparation.definitions

import com.jonasgerdes.stoppelmap.dto.data.Tag
import com.jonasgerdes.stoppelmap.preparation.definitions.TagSlugs.forKids
import com.jonasgerdes.stoppelmap.preparation.definitions.TagSlugs.vegan
import com.jonasgerdes.stoppelmap.preparation.definitions.TagSlugs.wheelchairAccessible
import com.jonasgerdes.stoppelmap.preparation.localizedString

object TagSlugs {
    const val forKids = "for_kids"
    const val wheelchairAccessible = "wheelchair_accessible"
    const val vegan = "vegan"
}

val tags = listOf(
    Tag(
        slug = forKids,
        name = localizedString(de = "für Kinder", en = "for kids"),
    ),
    Tag(
        slug = wheelchairAccessible,
        name = localizedString(de = "rollstuhlgerecht", en = "wheelchair accessible")
    ),
    Tag(
        slug = vegan,
        name = localizedString(de = "vegane Optionen", en = "vegane Optionen")
    ),
)