package com.jonasgerdes.stoppelmap.util

import android.content.res.AssetManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */

fun AssetManager.readAsString(path: String): String {
    try {
        val sourceStream = open(path)
        val buffer = ByteArray(sourceStream.available())
        sourceStream.read(buffer)
        sourceStream.close()
        return buffer.toString(Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return ""
    }

}

fun AssetManager.copyToFile(path: String, destination: File) {
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