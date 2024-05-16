package com.jonasgerdes.stoppelmap.home.ui

import com.jonasgerdes.stoppelmap.licenses.data.LicensesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LicensesDependencies : KoinComponent {
    val lincensesRepository: LicensesRepository by inject()
}