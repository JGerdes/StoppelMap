package com.jonasgerdes.stoppelmap.preparation.operations

import app.cash.sqldelight.db.SqlDriver
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.conversion.usecase.UpdateDatabaseUseCase
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.preparation.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VerifyStoppelMapDataConversion : KoinComponent {

    private val driver: SqlDriver by inject()
    private val settings: Settings by inject()
    private val updateData: UpdateDatabaseUseCase by inject()

    operator fun invoke(data: StoppelMapData) {
        if (!settings.databaseFile.exists()) {
            settings.databaseFile.createNewFile()
            StoppelMapDatabase.Schema.create(driver)
        }
        updateData(data)
    }
}