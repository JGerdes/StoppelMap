package com.jonasgerdes.stoppelmap.data.repository

import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase

class StallRepository(
    private val database: StoppelmapDatabase
) {

    suspend fun queryStallsByName(nameQuery: String) =
        database.stallsDao().findStallByName("%$nameQuery%")
}