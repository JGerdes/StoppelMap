package com.jonasgerdes.stoppelmap.map.data

import com.jonasgerdes.stoppelmap.data.map.Sub_typeQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SubTypeRepository(
    private val subTypeQueries: Sub_typeQueries
) {

    suspend fun searchByName(query: String) = withContext(Dispatchers.IO) {
        subTypeQueries.searchByName(query).executeAsList()
    }

    suspend fun searchByAlias(query: String) = withContext(Dispatchers.IO) {
        subTypeQueries.searchByAlias(query).executeAsList()
    }

}