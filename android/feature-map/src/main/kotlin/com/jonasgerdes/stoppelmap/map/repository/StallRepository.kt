package com.jonasgerdes.stoppelmap.map.repository

import com.jonasgerdes.stoppelmap.data.StallQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StallRepository(
    private val stallQueries: StallQueries
) {

    suspend fun getStall(slug: String) = withContext(Dispatchers.IO) {
        stallQueries.selectBySlug(slug).executeAsOneOrNull()
    }

    suspend fun findByName(nameQuery: String) = withContext(Dispatchers.IO) {
        stallQueries.findByNameQuery(nameQuery).executeAsList()
    }

    suspend fun findByType(typeSlug: String) = withContext(Dispatchers.IO) {
        stallQueries.findByType(typeSlug).executeAsList()
    }

    suspend fun findBySubType(typeSlug: String) = withContext(Dispatchers.IO) {
        stallQueries.selectStallsBySubType(typeSlug).executeAsList()
    }

    suspend fun findByItem(itemSlug: String) = withContext(Dispatchers.IO) {
        stallQueries.selectStallsByItem(itemSlug).executeAsList()
    }
}
