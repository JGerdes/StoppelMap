package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import android.content.Context
import okio.Path

actual class RemoveDatabaseFileUseCase(private val context: Context) {
    actual suspend operator fun invoke(path: Path) {
        context.deleteDatabase(path.toFile().nameWithoutExtension)
    }
}