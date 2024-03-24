package com.jonasgerdes.stoppelmap.dataupdate.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun InputStream.copyToFile(destination: File) {
    destination.parentFile?.mkdirs()
    val destinationStream = FileOutputStream(destination)
    val buf = ByteArray(1024)
    var bytes = read(buf)
    while (bytes > 0) {
        destinationStream.write(buf, 0, bytes)
        bytes = read(buf)
    }
    close()
}

fun Context.removeDatabase(name: String) {
    val dir = File(filesDir.parentFile, "databases")

    listOf(".db", ".db-shm", ".db-wal")
        .map { File(dir, name + it) }
        .filter { it.exists() }
        .forEach { it.delete() }

}
