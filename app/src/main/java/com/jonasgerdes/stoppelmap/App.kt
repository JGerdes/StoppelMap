package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jonasgerdes.stoppelmap.admin.Administration
import de.jonasrottmann.realmbrowser.RealmBrowser
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build())

        Administration.init(this)
        RealmBrowser.addFilesShortcut(this)
    }
}