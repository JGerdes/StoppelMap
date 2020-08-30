package com.jonasgerdes.stoppelmap.di

import android.content.Context
import com.jonasgerdes.androidutil.navigation.FragmentFactory
import com.jonasgerdes.stoppelmap.BuildConfig
import com.jonasgerdes.stoppelmap.core.domain.*
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.domain.FusedLocationLocationProvider
import com.jonasgerdes.stoppelmap.domain.StoppelmarktInfoProvider
import com.jonasgerdes.stoppelmap.view.FragmentFactoryImpl
import dagger.Module
import dagger.Provides

@Module
class StoppelmapModule {

    @Provides
    fun dateTimeProvider(): DateTimeProvider =
        if (BuildConfig.DEBUG) NowAtStomaSaturdayTimeProvider()
        else LiveDateTimeProvider()

    @Provides
    fun locationProvider(context: Context): LocationProvider =
        FusedLocationLocationProvider(context)

    @Provides
    fun globalInfoProvider(): GlobalInfoProvider = StoppelmarktInfoProvider()

    @Provides
    fun fragmentFactory(): FragmentFactory<Route> = FragmentFactoryImpl()

}