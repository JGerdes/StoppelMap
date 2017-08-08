package com.jonasgerdes.stoppelmap.util.versioning

import android.content.Context
import com.google.gson.GsonBuilder
import com.jonasgerdes.stoppelmap.BuildConfig
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.jonasgerdes.stoppelmap.model.versioning.VersionInfo
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16.06.2017
 */
class VersionHelper(val app: Context) {

    companion object {
        private const val PREF_VERSION_CONFIG = "STOPPELMAP_VERSION_CONFIG"
    }

    private val okHttpClient = OkHttpClient()
    private val gson = GsonBuilder().create()

    private val pref = app.getSharedPreferences(PREF_VERSION_CONFIG, Context.MODE_PRIVATE)

    val versionCode = app.packageManager.getPackageInfo(app.packageName, 0).versionCode
    val versionName = app.packageManager.getPackageInfo(app.packageName, 0).versionName

    val isStoreBuild = BuildConfig.FLAVOR == "store"

    fun requestVersionInfo(): Observable<VersionInfo> {
        val appName = app.getString(R.string.app_name)
        val userAgent = "$appName v$versionName ($versionCode)"
        val url = app.getString(R.string.url_check_version)

        val request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", userAgent)
                .build()

        return Observable.create {
            val response = okHttpClient.newCall(request).execute()
            val jsonData = response.body()?.string()
            val versionInfo = gson.fromJson(jsonData, VersionInfo::class.java)
            versionInfo.latest = versionInfo.version["release"]
            it.onNext(versionInfo)
            it.onComplete()
        }
    }

    fun getHasMessageBeShown(message: Message): Boolean {
        val pref = app.getSharedPreferences("STOPPELMAP_SHOW_MESSAGES", Context.MODE_PRIVATE)
        return pref.getBoolean(message.slug, false)
    }

    fun setHasMessageBeShown(message: Message) {
        val editor = app.getSharedPreferences("STOPPELMAP_SHOW_MESSAGES", Context.MODE_PRIVATE)
                .edit()
        editor.putBoolean(message.slug, true).apply()
    }

}