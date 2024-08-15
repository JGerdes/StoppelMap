package com.jonasgerdes.stoppelmap.map.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
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

    fun getAll() = tagQueries.getAllWithName(::Tag).asFlow().mapToList(Dispatchers.IO)

}