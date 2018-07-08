package com.jonasgerdes.stoppelmap.domain.model

sealed class ExternalIntent {
    object None : ExternalIntent()
    data class Url(val url: String) : ExternalIntent()
}