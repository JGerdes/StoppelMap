package com.jonasgerdes.stoppelmap.data.repository

import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.SubType

class StallRepository(
    private val database: StoppelmapDatabase
) {

    suspend fun getStallBySlug(slug: String) =
        database.stallsDao().findStallBySlug(slug)

    suspend fun queryStallsByName(nameQuery: String) =
        database.stallsDao().findStallByName("%$nameQuery%")

    suspend fun findStallByAlias(stallAlias: String): List<StallWithAlias> {
        return database.stallsDao().findAlias("%$stallAlias%").map { alias ->
            StallWithAlias(
                stall = database.stallsDao().findStallBySlug(alias.stall)!!,
                alias = alias.alias
            )
        }
    }

    suspend fun findStallsByType(typeQuery: String): List<TypeWithStalls> {
        val types = database.stallTypeDao().findTypeByName("%$typeQuery%")
        return types.map { type ->
            val stallsWithType = database.stallsDao().getStallsByType(type.slug)
            val stallsWithSubtype = database.stallsDao().getStallsBySubType(type.slug)
            TypeWithStalls(type, stallsWithType.union(stallsWithSubtype).toList())
        }
    }

    suspend fun findStallsByItem(itemQuery: String): List<ItemWithStalls> {
        val items = database.itemDao().findItemByName("%$itemQuery%")
        return items.map { item ->
            val stallsWithItem = database.stallsDao().getStallsByItem(item.slug)
            ItemWithStalls(item, stallsWithItem)
        }
    }


    data class StallWithAlias(val stall: Stall, val alias: String)
    data class TypeWithStalls(val type: SubType, val stalls: List<Stall>)
    data class ItemWithStalls(val item: Item, val stalls: List<Stall>)
}