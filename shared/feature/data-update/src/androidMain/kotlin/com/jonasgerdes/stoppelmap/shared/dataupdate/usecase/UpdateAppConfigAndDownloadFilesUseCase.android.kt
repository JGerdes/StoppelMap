package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import com.jonasgerdes.stoppelmap.dto.config.Data


actual fun Data.SupportedSince.onCurrentPlatform(): Int = android