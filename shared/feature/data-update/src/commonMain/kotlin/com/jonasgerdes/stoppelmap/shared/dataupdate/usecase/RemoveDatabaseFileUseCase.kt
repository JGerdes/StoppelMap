package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import okio.Path


expect class RemoveDatabaseFileUseCase {
    suspend operator fun invoke(path: Path)
}