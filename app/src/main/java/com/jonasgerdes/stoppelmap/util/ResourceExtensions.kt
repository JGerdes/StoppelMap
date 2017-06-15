package com.jonasgerdes.stoppelmap.util

import android.content.res.AssetManager
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */

fun AssetManager.readAsString(path: String): String {
    try {
        val inputStream = open(path)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return buffer.toString(Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return ""
    }

}