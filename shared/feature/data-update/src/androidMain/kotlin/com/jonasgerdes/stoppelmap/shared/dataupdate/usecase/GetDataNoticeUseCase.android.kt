package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import com.jonasgerdes.stoppelmap.dto.Platform

actual fun Platform.isCurrentPlatform() = this == Platform.Android