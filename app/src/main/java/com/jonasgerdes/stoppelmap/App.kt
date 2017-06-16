package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jonasgerdes.stoppelmap.admin.Administration
import com.jonasgerdes.stoppelmap.di.AppComponent
import com.jonasgerdes.stoppelmap.di.DaggerAppComponent
import com.jonasgerdes.stoppelmap.di.module.AppModule
import com.jonasgerdes.stoppelmap.util.versioning.VersionHelper
import de.jonasrottmann.realmbrowser.RealmBrowser
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class App : Application(), AnkoLogger {

    companion object {
        lateinit var graph: AppComponent
    }

    @Inject
    lateinit var version: VersionHelper

    override fun onCreate() {
        super.onCreate()

        graph = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()

        graph.inject(this)

        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build())

        Administration.init(this)
        RealmBrowser.addFilesShortcut(this)


        //todo: put somewhere else
        if (version.installedDatabaseVersion < version.providedDatabaseVersion) {
            info("updating database from ${version.installedDatabaseVersion}" +
                    " to ${version.providedDatabaseVersion}")
        }
    }
}