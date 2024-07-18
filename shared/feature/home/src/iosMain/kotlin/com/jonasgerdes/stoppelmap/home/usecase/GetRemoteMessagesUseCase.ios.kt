package com.jonasgerdes.stoppelmap.home.usecase

import com.jonasgerdes.stoppelmap.dto.Platform

actual fun Platform.isCurrentPlatform(): Boolean = this == Platform.iOS