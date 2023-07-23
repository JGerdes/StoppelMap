package com.jonasgerdes.stoppelmap.base.contract

data class AppInfo(
    val versionName: String,
    val versionCode: Int,
    val buildType: String,
    val commitSha: String,
)
