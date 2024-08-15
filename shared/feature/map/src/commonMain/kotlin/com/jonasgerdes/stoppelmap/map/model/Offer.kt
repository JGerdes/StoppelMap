package com.jonasgerdes.stoppelmap.map.model

data class Offer(
    val productSlug: String,
    val name: String,
    val modifier: String?,
    val price: Long?
) {
    companion object {
        val barProducts = setOf("item_beer", "item_softdrinks", "item_shots", "item_cocktails", "item_longdrinks")
        val imbissProducts = setOf("item_fries", "item_sausage")
    }
}