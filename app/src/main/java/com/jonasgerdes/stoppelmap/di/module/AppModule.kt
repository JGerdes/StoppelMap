package com.jonasgerdes.stoppelmap.di.module

import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.util.versioning.VersionHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
@Module
class AppModule(private val app: App) {

}