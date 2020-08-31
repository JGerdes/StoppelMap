package com.jonasgerdes.stoppelmap.core.domain

import android.content.Context
import com.jonasgerdes.androidutil.versionCode
import com.jonasgerdes.androidutil.versionName
import javax.inject.Inject

class AppVersionProvider
@Inject
constructor(private val context: Context) {

    val getAppVersionName
        get() = context.versionName

    val getAppVersionCode
        get() = context.versionCode
}