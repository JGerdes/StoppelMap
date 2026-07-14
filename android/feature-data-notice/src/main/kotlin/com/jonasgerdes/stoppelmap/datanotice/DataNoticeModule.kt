package com.jonasgerdes.stoppelmap.datanotice


import com.jonasgerdes.stoppelmap.shared.dataupdate.ui.DataNoticeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataNoticeModule = module {
    viewModel {
        DataNoticeViewModel(
            getDataNotice = get()
        )
    }
}
