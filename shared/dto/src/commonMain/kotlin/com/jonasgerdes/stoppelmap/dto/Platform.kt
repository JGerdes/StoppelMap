package com.jonasgerdes.stoppelmap.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Platform {

    @SerialName("android")
    Android,

    @SerialName("iOS")
    iOS
}