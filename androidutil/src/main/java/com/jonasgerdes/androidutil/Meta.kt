package com.jonasgerdes.androidutil

import android.content.Context
import android.content.Intent
import android.net.Uri

val Context.versionName
    get() = packageManager.getPackageInfo(packageName, 0).versionName

val Context.versionCode
    get() = packageManager.getPackageInfo(packageName, 0).versionCode

fun Context.openPlaystore() {
    val packageName = applicationContext.applicationInfo.packageName
    val url = "https://play.google.com/store/apps/details?id=$packageName"
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}