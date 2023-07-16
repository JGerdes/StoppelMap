package com.jonasgerdes.stoppelmap.settings.data

import androidx.annotation.DrawableRes

data class ImageSource(
    val work: String,
    val author: String,
    val sourceUrl: String,
    val license: License? = null,
    val website: String? = null,
    @DrawableRes val resource: Int? = null
)

data class ImageSources(
    val imageSources: List<ImageSource>
)
