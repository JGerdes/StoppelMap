package com.jonasgerdes.stoppelmap.di

import androidx.lifecycle.ViewModelProvider
import com.jonasgerdes.stoppelmap.core.di.ViewModelModule
import com.jonasgerdes.stoppelmap.home.di.HomeModule
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = [
        ContextModule::class,
        StoppelmapModule::class,
        ViewModelModule::class,
        HomeModule::class
    ]
)
@Singleton
interface AppComponent {
    fun viewModelFactory(): ViewModelProvider.Factory
}