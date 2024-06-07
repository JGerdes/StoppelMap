package com.jonasgerdes.stoppelmap.server.news.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetArticlesResponse(
    val articles: List<Article>,
    val pagination: Pagination,
)

@Serializable
data class Pagination(
    val previous: String? = null,
    val next: String? = null,
)