package com.jonasgerdes.stoppelmap

import com.jonasgerdes.androidutil.navigation.FragmentFactory
import com.jonasgerdes.stoppelmap.core.domain.*
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.domain.FusedLocationLocationProvider
import com.jonasgerdes.stoppelmap.domain.StoppelmarktInfoProvider
import com.jonasgerdes.stoppelmap.view.FragmentFactoryImpl
import org.koin.dsl.module


val appModule = module {
    single<DateTimeProvider> {
        if (BuildConfig.DEBUG) NowAtStomaSaturdayTimeProvider() else LiveDateTimeProvider()
    }

    single<LocationProvider> { FusedLocationLocationProvider(context = get()) }
    single<GlobalInfoProvider> { StoppelmarktInfoProvider() }
    single<FragmentFactory<Route>> { FragmentFactoryImpl() }
}