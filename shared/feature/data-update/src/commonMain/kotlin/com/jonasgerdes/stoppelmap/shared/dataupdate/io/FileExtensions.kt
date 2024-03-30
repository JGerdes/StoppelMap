package com.jonasgerdes.stoppelmap.shared.dataupdate.io

import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import okio.Path

expect fun DatabaseFile.toPath(): Path
expect fun MapDataFile.toPath(): Path
