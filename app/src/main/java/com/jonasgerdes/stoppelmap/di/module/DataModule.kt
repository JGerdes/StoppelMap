package com.jonasgerdes.stoppelmap.di.module

import com.jonasgerdes.stoppelmap.model.MapEntityRepository
import com.jonasgerdes.stoppelmap.model.QueryFactory
import dagger.Module
import dagger.Provides

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.06.2017
 */
@Module
class DataModule {

    @Provides
    fun provideMapEntityRepository(): MapEntityRepository {
        return MapEntityRepository()
    }

    @Provides
    fun provideQueryFactory(): QueryFactory {
        return QueryFactory()
    }
}