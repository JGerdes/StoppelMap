package com.jonasgerdes.stoppelmap.util.versioning

import android.content.Context
import com.google.gson.GsonBuilder
import com.jonasgerdes.stoppelmap.BuildConfig
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.jonasgerdes.stoppelmap.model.versioning.VersionInfo
import com.jonasgerdes.stoppelmap.util.versionCode
import com.jonasgerdes.stoppelmap.util.versionName
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request


class VersionProviderImpl(app: Context) : VersionProvider {

    companion object {
        private const val PREF_VERSION_CONFIG = "STOPPELMAP_VERSION_CONFIG"
        private const val URL_VERSION_CHECK = "https://app.stoppelmap.de/version"
        lateinit var instance: VersionProviderImpl

        fun init(app: Context) {
            instance = VersionProviderImpl(app)
        }
    }

    private val okHttpClient = OkHttpClient()
    private val gson = GsonBuilder().create()

    private val versionCode = app.versionCode
    private val versionName = app.versionName
    private val appName = app.getString(R.string.app_name)
    private val userAgent = "$appName v$versionName ($versionCode)"

    private val pref = app.getSharedPreferences("STOPPELMAP_SHOW_MESSAGES", Context.MODE_PRIVATE)

    override fun requestVersionInfo(): Observable<VersionInfo> {
        val url = URL_VERSION_CHECK

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

    override fun getHasMessageBeShown(message: Message): Boolean {
        return pref.getBoolean(message.slug, false)
    }

    override fun setHasMessageBeShown(message: Message) {
        val editor = pref.edit()
        editor.putBoolean(message.slug, true).apply()
    }

    override fun getCurrentVersionCode() = versionCode

}