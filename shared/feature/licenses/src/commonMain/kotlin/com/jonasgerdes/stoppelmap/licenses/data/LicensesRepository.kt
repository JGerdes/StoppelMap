package com.jonasgerdes.stoppelmap.licenses.data

import com.jonasgerdes.stoppelmap.licenses.model.ImageSource
import com.jonasgerdes.stoppelmap.licenses.model.Library

class LicensesRepository(
    private val libraries: List<Library>,
    private val imageSources: List<ImageSource>,
) {

    fun getLibraries() = libraries
    fun getImageSources() = imageSources
}