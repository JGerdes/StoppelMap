package com.jonasgerdes.stoppelmap.base.model

data class AppInfo(
    val versionName: String,
    val versionCode: Int,
    val buildType: String,
    val commitSha: String,
    val platform: String,
    val os: String,
    val device: String,
) {
    val userAgent get() = "StoppelMap $versionName $platform"
}
