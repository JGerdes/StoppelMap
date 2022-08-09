package com.jonasgerdes.stoppelmap.preparation

import java.io.File

data class Settings(
    val databaseFile: File,
    val geoJsonInput: File,
    val geoJsonOutput: File,
)
