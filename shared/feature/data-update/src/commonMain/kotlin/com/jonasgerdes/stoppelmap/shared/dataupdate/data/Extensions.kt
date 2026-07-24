package com.jonasgerdes.stoppelmap.shared.dataupdate.data

import com.jonasgerdes.stoppelmap.dto.Platform
import com.jonasgerdes.stoppelmap.dto.config.Data

expect fun Data.SupportedSince.onCurrentPlatform(): Int
expect fun Platform.isCurrentPlatform(): Boolean
