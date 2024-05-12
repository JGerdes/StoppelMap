package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import com.jonasgerdes.stoppelmap.shared.dataupdate.model.Data

actual fun Data.SupportedSince.onCurrentPlatform(): Int = iOS