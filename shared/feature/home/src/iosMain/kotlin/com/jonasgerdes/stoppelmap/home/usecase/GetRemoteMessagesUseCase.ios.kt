package com.jonasgerdes.stoppelmap.home.usecase

import com.jonasgerdes.stoppelmap.shared.dataupdate.model.Platform

actual fun Platform.isCurrentPlatform(): Boolean = this == Platform.iOS