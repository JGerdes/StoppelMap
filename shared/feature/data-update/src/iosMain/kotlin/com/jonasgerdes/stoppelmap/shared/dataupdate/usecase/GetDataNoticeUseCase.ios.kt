package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import com.jonasgerdes.stoppelmap.dto.Platform

actual fun Platform.isCurrentPlatform(): Boolean = this == Platform.iOS