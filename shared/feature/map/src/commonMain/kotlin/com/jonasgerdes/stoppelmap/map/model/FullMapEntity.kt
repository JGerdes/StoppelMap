package com.jonasgerdes.stoppelmap.map.model

import com.jonasgerdes.stoppelmap.data.map.MapEntityType
import com.jonasgerdes.stoppelmap.map.model.Offer.Companion.barProducts

data class FullMapEntity(
    val slug: String,
    val name: String,
    val typeName: String?,
    val type: MapEntityType,
    val subType: String?,
    val description: String?,
    val location: Location,
    val bounds: BoundingBox,
    val icon: MapIcon,
    val offers: List<Offer>,
    val tags: List<Tag>,
    val admissionFees: List<Fee>,
    val images: List<Image>,
    val websites: List<Website>,
    val events: List<Event>,
) {
    fun subline() = when {
        type == MapEntityType.FoodStall && offers.any { barProducts.contains(it.productSlug) } -> "Imbiss mit Ausschank" //TODO: localize
        subType != null && typeName != null -> "$subType ($typeName)"
        typeName != null -> typeName
        else -> null
    }
}
