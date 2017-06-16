package com.jonasgerdes.stoppelmap.util.versioning

import android.content.Context
import com.jonasgerdes.stoppelmap.BuildConfig

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
class VersionHelper(app: Context) {

    companion object {
        private const val PREF_VERSION_CONFIG = "STOPPELMAP_VERSION_CONFIG"
        private const val REALM_VERSION = "REALM_VERSION"
    }

    private val pref = app.getSharedPreferences(PREF_VERSION_CONFIG, Context.MODE_PRIVATE)

    val versionCode = app.packageManager.getPackageInfo(app.packageName, 0).versionCode
    val versionName = app.packageManager.getPackageInfo(app.packageName, 0).versionName

    val providedDatabaseVersion = BuildConfig.providedDatabaseVersion
    var installedDatabaseVersion
        get() = pref.getInt(REALM_VERSION, 0)
        set(value) {
            val editor = pref.edit()
            editor.putInt(REALM_VERSION, value)
            editor.apply()
        }

}