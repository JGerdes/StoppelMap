package com.jonasgerdes.stoppelmap.preparation.operations

import app.cash.sqldelight.db.SqlDriver
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.preparation.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrepareDatabase : KoinComponent {

    private val settings: Settings by inject()
    private val driver: SqlDriver by inject()

    operator fun invoke() {
        if (settings.databaseFile.exists()) {
            settings.databaseFile.delete()
        }
        //settings.databaseFile.mkdirs()
        settings.databaseFile.createNewFile()
        StoppelMapDatabase.Schema.create(driver)
    }
}
