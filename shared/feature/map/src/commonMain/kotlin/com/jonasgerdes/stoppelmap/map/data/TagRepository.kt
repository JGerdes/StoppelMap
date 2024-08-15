package com.jonasgerdes.stoppelmap.map.data

import com.jonasgerdes.stoppelmap.data.shared.TagQueries
import com.jonasgerdes.stoppelmap.map.model.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class TagRepository(
    private val tagQueries: TagQueries,
) {

    suspend fun search(query: String) = withContext(Dispatchers.IO) {
        tagQueries.searchByName(query, ::Tag).executeAsList() +
                tagQueries.searchByAlias(query, ::Tag).executeAsList()
    }

}