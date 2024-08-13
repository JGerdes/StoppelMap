package com.jonasgerdes.stoppelmap.transportation.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jonasgerdes.stoppelmap.data.shared.ServiceQueries
import com.jonasgerdes.stoppelmap.transportation.model.TaxiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaxiServiceRepository(
    private val serviceQueries: ServiceQueries,
) {

    fun getTaxiServices(): Flow<List<TaxiService>> =
        serviceQueries.getAllSlugs().asFlow().mapToList(Dispatchers.IO)
            .map {
                it.filter { it.startsWith("taxi_") }
                    .map {
                        serviceQueries.getBySlug(it).executeAsOne().let {
                            TaxiService(
                                title = it.name,
                                phoneNumber = it.phoneNumber,
                                phoneNumberFormatted = it.formattedNumber
                            )
                        }
                    }
                    .sortedBy { it.title }
            }
}
