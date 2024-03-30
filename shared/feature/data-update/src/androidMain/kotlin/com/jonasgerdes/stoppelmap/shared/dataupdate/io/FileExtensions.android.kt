package com.jonasgerdes.stoppelmap.shared.dataupdate.io

import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import okio.Path
import okio.Path.Companion.toPath

actual fun DatabaseFile.toPath(): Path {
    return file.absolutePath.toPath()
}

actual fun MapDataFile.toPath(): Path {
    return file.absolutePath.toPath()
}