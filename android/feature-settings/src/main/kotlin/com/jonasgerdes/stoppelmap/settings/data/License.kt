package com.jonasgerdes.stoppelmap.settings.data

open class License(
    val name: String,
    val fullText: String? = null,
    val link: String
) {

    data class Apache2(val url: String = "https://www.apache.org/licenses/LICENSE-2.0.txt") :
        License("Apache 2.0", "", url)

    data class BSD2Clause(val url: String = "https://opensource.org/licenses/BSD-2-Clause") :
        License("BSD 2-Clause", "", url)

    data class MIT(val url: String = "https://opensource.org/licenses/MIT") :
        License("MIT", "", url)

    data class Unsplash(val url: String = "https://unsplash.com/license") :
        License("Unsplash", "", url)
}
