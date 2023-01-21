package com.jonasgerdes.stoppelmap.map.repository

import com.jonasgerdes.stoppelmap.data.Sub_typesQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TypeRepository(
    private val typeQueries: Sub_typesQueries
) {
    
    suspend fun findByName(nameQuery: String) = withContext(Dispatchers.IO) {
        typeQueries.findByQuery(nameQuery).executeAsList()
    }

}
