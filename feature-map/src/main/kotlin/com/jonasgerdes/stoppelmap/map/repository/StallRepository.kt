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
}
