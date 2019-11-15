package com.jonasgerdes.stoppelmap.di

import androidx.lifecycle.ViewModelProvider
import com.jonasgerdes.stoppelmap.core.di.Injector

class InjectorImpl(private val appComponent: AppComponent) : Injector {
    override val viewModelFactory: ViewModelProvider.Factory
        get() = appComponent.viewModelFactory()
}