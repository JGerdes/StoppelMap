package com.jonasgerdes.stoppelmap.util.versioning

import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.jonasgerdes.stoppelmap.model.versioning.VersionInfo
import io.reactivex.Observable

interface VersionProvider {

    fun requestVersionInfo(): Observable<VersionInfo>

    fun getHasMessageBeShown(message: Message): Boolean

    fun setHasMessageBeShown(message: Message)

    fun getCurrentVersionCode(): Int

    fun getUserAgent(): String

}