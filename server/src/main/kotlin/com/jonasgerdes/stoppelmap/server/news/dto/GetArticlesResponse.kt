package com.jonasgerdes.stoppelmap.server.news.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetArticlesResponse(
    val articles: List<Article>,
)