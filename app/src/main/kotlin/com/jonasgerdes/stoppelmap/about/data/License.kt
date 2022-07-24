package com.jonasgerdes.stoppelmap.about.data

open class License(
    val name: String,
    val fullText: String? = null,
    val link: String
) {

    data class Apache2(val url: String = "https://www.apache.org/licenses/LICENSE-2.0.txt") :
        License("Apache 2.0", "", url)

    data class MIT(val url: String = "https://opensource.org/licenses/MIT") :
        License("MIT", "", url)
}
