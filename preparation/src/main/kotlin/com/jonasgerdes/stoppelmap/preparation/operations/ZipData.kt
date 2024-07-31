package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.preparation.Settings
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@OptIn(ExperimentalSerializationApi::class)
class ZipData : KoinComponent {

    private val settings: Settings by inject()

    operator fun invoke() {
        val filesToZip = settings.tempDir.listFiles().asList()
        ZipOutputStream(File(settings.dataOutputDir, "data.zip").outputStream().buffered()).use { zipStream ->
            filesToZip.forEach { file ->
                file.inputStream().buffered().use { fileStream ->
                    zipStream.putNextEntry(ZipEntry(file.name))
                    println("Zipping $file")
                    fileStream.copyTo(zipStream)
                }
            }
        }
    }
}