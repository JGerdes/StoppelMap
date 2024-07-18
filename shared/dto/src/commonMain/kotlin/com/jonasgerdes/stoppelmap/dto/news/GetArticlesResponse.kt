package com.jonasgerdes.stoppelmap.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class GetArticlesResponse(
    val articles: List<Article>,
)