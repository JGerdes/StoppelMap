package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.AttrRes
import android.util.TypedValue


val Context.versionName
    get() = packageManager.getPackageInfo(packageName, 0).versionName

val Context.versionCode
    get() = packageManager.getPackageInfo(packageName, 0).versionCode

fun Context.openPlaystore() {
    val packageName = applicationContext.applicationInfo.packageName
    val url = "https://play.google.com/store/apps/details?id=$packageName"
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun Context.getValueFromTheme(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}