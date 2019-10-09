package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.data.like
import com.jonasgerdes.stoppelmap.model.map.Item

class ItemDaoStub(private val items: MutableList<Item>) : ItemDao() {

    override suspend fun findItemByName(name: String): List<Item> = items.like(name) { it.name }

    override suspend fun getItemBySlug(itemSlug: String): Item? =
        items.first { it.slug == itemSlug }
}