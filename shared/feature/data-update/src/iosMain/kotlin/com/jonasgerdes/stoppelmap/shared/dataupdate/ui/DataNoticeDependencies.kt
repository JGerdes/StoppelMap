package com.jonasgerdes.stoppelmap.shared.dataupdate.ui

import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.GetDataNoticeUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DataNoticeDependencies : KoinComponent {
    val getDataNoticeUseCase: GetDataNoticeUseCase by inject()
}