package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import okio.FileSystem
import okio.Path

actual class RemoveDatabaseFileUseCase {
    actual suspend operator fun invoke(path: Path) {
        FileSystem.SYSTEM.delete(path)
    }
}