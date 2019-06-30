package com.jonasgerdes.stoppelmap

import com.jonasgerdes.androidutil.navigation.FragmentFactory
import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import com.jonasgerdes.stoppelmap.core.domain.LiveDateTimeProvider
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.view.FragmentFactoryImpl
import org.koin.dsl.module


val appModule = module {
    single<DateTimeProvider> { LiveDateTimeProvider() }
    single<FragmentFactory<Route>> { FragmentFactoryImpl() }
}