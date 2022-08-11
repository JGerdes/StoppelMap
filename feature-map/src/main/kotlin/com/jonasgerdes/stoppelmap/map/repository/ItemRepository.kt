package com.jonasgerdes.stoppelmap.map.repository

import com.jonasgerdes.stoppelmap.data.ItemQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(
    private val itemQueries: ItemQueries,
) {

    suspend fun findByName(nameQuery: String) = withContext(Dispatchers.IO) {
        itemQueries.findByNameQuery(nameQuery).executeAsList()
    }

}
