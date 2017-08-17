package com.jonasgerdes.stoppelmap

import android.support.multidex.MultiDexApplication
import com.jonasgerdes.stoppelmap.admin.Administration
import com.jonasgerdes.stoppelmap.di.AppComponent
import com.jonasgerdes.stoppelmap.di.DaggerAppComponent
import com.jonasgerdes.stoppelmap.di.module.AppModule
import com.jonasgerdes.stoppelmap.di.module.DataModule
import com.jonasgerdes.stoppelmap.util.versioning.VersionHelper
import de.jonasrottmann.realmbrowser.RealmBrowser
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class App : MultiDexApplication(), AnkoLogger {

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
                .dataModule(DataModule())
                .build()

        graph.inject(this)

        Realm.init(this)
        Realm.setDefaultConfiguration(createRealmConfig())

        Administration.init(this)
        RealmBrowser.addFilesShortcut(this)
    }

    private fun createRealmConfig(): RealmConfiguration {
        val builder = RealmConfiguration.Builder()
        return if (version.isStoreBuild) {
            builder.assetFile("data.realm")
            val config = builder.build()
            Realm.deleteRealm(config)
            config
        } else {
            builder.deleteRealmIfMigrationNeeded()
                    .build()
        }
    }
}