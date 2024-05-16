package com.jonasgerdes.stoppelmap.settings

import com.jonasgerdes.stoppelmap.licenses.ui.LicensesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidLicensesModule = module {

    viewModel {
        LicensesViewModel(
            licensesRepository = get(),
        )
    }
}
