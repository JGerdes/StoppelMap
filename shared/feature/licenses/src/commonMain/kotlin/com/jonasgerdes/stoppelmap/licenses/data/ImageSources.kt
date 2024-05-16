package com.jonasgerdes.stoppelmap.licenses.data

import com.jonasgerdes.stoppelmap.licenses.model.ImageSource
import com.jonasgerdes.stoppelmap.licenses.model.License

internal val commonImageSources = listOf(
    ImageSource(
        author = "Erwan Hesry",
        work = "Feuerwerk",
        sourceUrl = "https://unsplash.com/photos/WPTHZkA-M4I",
        license = License.Unsplash(),
        website = "https://unsplash.com/@erwanhesry",
        // TODO? somehow provide this
        //resource = R.drawable.fireworks
    )
)