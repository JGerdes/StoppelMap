package com.jonasgerdes.stoppelmap.map.usecase

import android.content.Context
import com.mapbox.maps.ResourceOptionsManager

class InitializeMapBoxUseCase {

    operator fun invoke(context: Context) {
        ResourceOptionsManager.getDefault(
            context,
            // We don't load any data since we provide it locally - no token needed
            defaultToken = ""
        )
    }
}
