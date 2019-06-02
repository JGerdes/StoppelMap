package com.jonasgerdes.stoppelmap.news.data.repository

import com.jonasgerdes.stoppelmap.news.data.entity.Article
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

interface NewsRepository {

    fun getNews(): ReceiveChannel<List<Article>>
    suspend fun loadNextPage(): LoadPageResult


    sealed class LoadPageResult {
        object Success: LoadPageResult()
        object Error: LoadPageResult()
        object NetworkError: LoadPageResult()
    }
}