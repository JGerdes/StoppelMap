package com.jonasgerdes.stoppelmap.preparation

import java.io.File

data class Settings(
    val databaseFile: File,
    val geoJsonInput: File,
    val dataOutputDir: File,
    val tempDir: File,
    val fetchedEventsFile: File,
    val eventLocationsFile: File,
    val manualEventsFile: File,
    val crawledRoutesDirectory: File,
    val descriptionFolder: File,

    val year: Int,
)
