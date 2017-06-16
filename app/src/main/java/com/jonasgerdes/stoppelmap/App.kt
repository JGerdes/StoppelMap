package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jonasgerdes.stoppelmap.admin.Administration
import com.jonasgerdes.stoppelmap.di.AppComponent
import com.jonasgerdes.stoppelmap.di.module.AppModule
import com.jonasgerdes.stoppelmap.di.DaggerAppComponent
import de.jonasrottmann.realmbrowser.RealmBrowser
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class App : Application() {

    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        graph = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()

        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build())

        Administration.init(this)
        RealmBrowser.addFilesShortcut(this)
    }
}