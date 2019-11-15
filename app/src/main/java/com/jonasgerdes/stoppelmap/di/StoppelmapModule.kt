package com.jonasgerdes.stoppelmap.di

import android.content.Context
import com.jonasgerdes.androidutil.navigation.FragmentFactory
import com.jonasgerdes.stoppelmap.BuildConfig
import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import com.jonasgerdes.stoppelmap.core.domain.LiveDateTimeProvider
import com.jonasgerdes.stoppelmap.core.domain.LocationProvider
import com.jonasgerdes.stoppelmap.core.domain.NowAtStomaSaturdayTimeProvider
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.domain.FusedLocationLocationProvider
import com.jonasgerdes.stoppelmap.domain.StoppelmarktInfoProvider
import com.jonasgerdes.stoppelmap.view.FragmentFactoryImpl
import dagger.Module
import dagger.Provides

@Module
class StoppelmapModule {

    @Provides
    fun dateTimeProvider() =
        if (BuildConfig.DEBUG) NowAtStomaSaturdayTimeProvider()
        else LiveDateTimeProvider()

    @Provides
    fun LocationProvider(context: Context): LocationProvider =
        FusedLocationLocationProvider(context)

    @Provides
    fun GlobalInfoProvider(): GlobalInfoProvider = StoppelmarktInfoProvider()

    @Provides
    fun FragmentFactory(): FragmentFactory<Route> = FragmentFactoryImpl()

}