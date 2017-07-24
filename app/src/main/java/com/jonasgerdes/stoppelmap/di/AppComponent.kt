package com.jonasgerdes.stoppelmap.di

import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.di.module.AppModule
import com.jonasgerdes.stoppelmap.di.module.DataModule
import com.jonasgerdes.stoppelmap.model.MapEntityRepository
import com.jonasgerdes.stoppelmap.usecase.map.view.MapFragment
import com.jonasgerdes.stoppelmap.usecase.map.view.search.ProductResultHolder
import com.jonasgerdes.stoppelmap.usecase.map.viewmodel.MapInteractor
import dagger.Component
import javax.inject.Singleton

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, DataModule::class))
interface AppComponent {
    fun inject(app: App)
    fun inject(interactor: MapInteractor)
    fun inject(mapFragment: MapFragment)
    fun inject(productResultHolder: ProductResultHolder)
    fun inject(mapEntityRepository: MapEntityRepository)
}