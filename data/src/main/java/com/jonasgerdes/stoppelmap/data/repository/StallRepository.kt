package com.jonasgerdes.stoppelmap.data.repository

import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import com.jonasgerdes.stoppelmap.model.map.Alias
import com.jonasgerdes.stoppelmap.model.map.Stall

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
                stall = database.stallsDao().findStallBySlug(alias.stall),
                alias = alias.alias
            )
        }
    }

    data class StallWithAlias(val stall: Stall, val alias: String)
}