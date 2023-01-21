package com.jonasgerdes.stoppelmap.map.repository

import com.jonasgerdes.stoppelmap.data.AliasQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AliasRepository(
    private val aliasQueries: AliasQueries,
) {

    suspend fun findByName(query: String) = withContext(Dispatchers.IO) {
        aliasQueries.findByQuery(query).executeAsList()
    }

}
