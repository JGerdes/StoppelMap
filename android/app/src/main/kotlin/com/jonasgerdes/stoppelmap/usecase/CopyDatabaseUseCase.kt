package com.jonasgerdes.stoppelmap.usecase

import android.content.Context
import com.jonasgerdes.stoppelmap.util.copyToFile
import com.jonasgerdes.stoppelmap.util.removeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class CopyDatabaseUseCase(
    private val context: Context,
    private val databaseFileName: String
) {

    suspend operator fun invoke() {
        withContext(Dispatchers.Default) {
            val dir = File(context.filesDir.parentFile, "databases")
            context.removeDatabase("stoma22")
            context.assets.copyToFile(databaseFileName, File(dir, databaseFileName))
        }
    }
}
