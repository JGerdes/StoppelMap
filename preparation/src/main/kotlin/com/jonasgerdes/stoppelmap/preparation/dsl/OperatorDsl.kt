package com.jonasgerdes.stoppelmap.preparation.dsl

import com.jonasgerdes.stoppelmap.dto.Localized
import com.jonasgerdes.stoppelmap.dto.data.Operator
import com.jonasgerdes.stoppelmap.dto.data.Website
import com.jonasgerdes.stoppelmap.preperation.asSlug

class OperatorScope {
    var websites = mutableListOf<Website>()
}

fun OperatorScope.website(
    url: String,
    label: Localized<String>? = null,
) {
    websites += Website(url = url, label = label)
}

fun operator(
    name: String,
    slug: String? = null,
    builder: (OperatorScope.() -> Unit)? = null
): Operator =
    OperatorScope().apply {
        builder?.invoke(this)
    }.let {
        Operator(
            slug = slug ?: "operator_${name.asSlug()}",
            name = name,
            websites = it.websites
        )
    }