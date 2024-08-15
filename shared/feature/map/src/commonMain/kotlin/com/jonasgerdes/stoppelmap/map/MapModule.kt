package com.jonasgerdes.stoppelmap.map

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.data.OfferRepository
import com.jonasgerdes.stoppelmap.map.data.SubTypeRepository
import com.jonasgerdes.stoppelmap.map.data.TagRepository
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
            offerQueries = get<StoppelMapDatabase>().offerQueries,
            mapMapTagQueries = get<StoppelMapDatabase>().map_entity_tagQueries,
        )
    }

    single {
        SubTypeRepository(subTypeQueries = get<StoppelMapDatabase>().sub_typeQueries)
    }

    single {
        OfferRepository(
            productQueries = get<StoppelMapDatabase>().productQueries,
            offerQueries = get<StoppelMapDatabase>().offerQueries,
        )
    }

    single {
        TagRepository(
            tagQueries = get<StoppelMapDatabase>().tagQueries,
        )
    }

    factory {
        SearchMapUseCase(
            mapEntityRepository = get(),
            subTypeRepository = get(),
            offerRepository = get(),
            tagRepository = get(),
        )
    }
}
