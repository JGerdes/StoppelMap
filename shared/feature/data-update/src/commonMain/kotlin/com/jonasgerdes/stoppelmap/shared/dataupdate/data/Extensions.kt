package com.jonasgerdes.stoppelmap.shared.dataupdate.data

import com.jonasgerdes.stoppelmap.dto.config.Data

expect fun Data.SupportedSince.onCurrentPlatform(): Int
