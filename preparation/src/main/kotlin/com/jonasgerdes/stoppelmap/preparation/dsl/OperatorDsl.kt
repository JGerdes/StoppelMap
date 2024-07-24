package com.jonasgerdes.stoppelmap.preparation.dsl

import com.jonasgerdes.stoppelmap.dto.data.Operator
import com.jonasgerdes.stoppelmap.preperation.asSlug

class OperatorScope {
    var websites = mutableListOf<String>()
}

fun operator(
    name: String,
    builder: (OperatorScope.() -> Unit)? = null
): Operator =
    OperatorScope().apply {
        builder?.invoke(this)
    }.let {
        Operator(
            slug = "operator_${name.asSlug()}",
            name = name,
            websites = it.websites
        )
    }