package com.jonasgerdes.stoppelmap.util

import android.content.res.AssetManager
import java.io.File
import java.io.FileOutputStream

fun AssetManager.copyToFile(path: String, destination: File) {
    destination.parentFile.mkdirs()
    val sourceStream = open(path)
    val destinationStream = FileOutputStream(destination)
    val buf = ByteArray(1024)
    var bytes = sourceStream.read(buf)
    while (bytes > 0) {
        destinationStream.write(buf, 0, bytes)
        bytes = sourceStream.read(buf)
    }
    sourceStream.close()
}