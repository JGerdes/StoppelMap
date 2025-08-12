package com.jonasgerdes.stoppelmap.preparation

import java.io.File

data class Settings(
    val databaseFile: File,
    val geoJsonInput: File,
    val dataOutputDir: File,
    val staticServerDir: File?,
    val tempDir: File,
    val fetchedEventsFile: File,
    val eventLocationsFile: File,
    val manualEventsFile: File,
    val crawledRoutesDirectory: File,
    val descriptionFolder: File,

    val year: Int,
    val imageSettings: ImageSettings?,
)

data class ImageSettings(
    val originalImageDir: File,
    val processedImageDir: File,
    val hostedImageBaseUrl: String,
) {
    companion object {
        fun fromEnv(): ImageSettings? {
            return ImageSettings(
                originalImageDir = System.getenv("IMAGE_ORIGINAL_DIR")?.let { File(it) } ?: return null,
                processedImageDir = System.getenv("IMAGE_PROCESSED_DIR")?.let { File(it) } ?: return null,
                hostedImageBaseUrl = System.getenv("IMAGE_HOST_BASE_URL") ?: return null,
            )
        }
    }
}
