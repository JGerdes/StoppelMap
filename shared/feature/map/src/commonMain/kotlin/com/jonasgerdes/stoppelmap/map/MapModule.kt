package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchMapUseCase
import org.koin.dsl.module

val mapModule = module {

    factory {
        GetMapFilePathUseCase(
            appUpdateRepository = get()
        )
    }

    single {
        MapEntityRepository(
            mapEntityQueries = get<StoppelMapDatabase>().map_entityQueries,
            aliasQueries = get<StoppelMapDatabase>().aliasQueries,
            subTypeQueries = get<StoppelMapDatabase>().sub_typeQueries,
        )
    }

    factory {
        SearchMapUseCase(
            mapEntityRepository = get()
        )
    }
}
