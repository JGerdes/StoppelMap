package com.jonasgerdes.stoppelmap.base.contract

abstract class PreferencesPathFactory {
    protected abstract fun createImpl(storageFile: String): String

    fun create(storageFileName: String) = createImpl(storageFileName + ".preferences_pb")
}