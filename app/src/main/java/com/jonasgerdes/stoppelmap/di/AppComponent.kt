package com.jonasgerdes.stoppelmap.di

import dagger.Component
import javax.inject.Singleton


@Component(
    modules = [
        ContextModule::class,
        StoppelmapModule::class
    ]
)
@Singleton
interface AppComponent