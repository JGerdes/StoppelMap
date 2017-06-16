package com.jonasgerdes.stoppelmap.di

import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: App)
}