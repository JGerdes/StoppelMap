package com.jonasgerdes.stoppelmap.shared.dataupdate.data

import com.jonasgerdes.stoppelmap.dto.Platform
import com.jonasgerdes.stoppelmap.dto.config.Data


actual fun Data.SupportedSince.onCurrentPlatform(): Int = android
actual fun Platform.isCurrentPlatform() = this == Platform.Android