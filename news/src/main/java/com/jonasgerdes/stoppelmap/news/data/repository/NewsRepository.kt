package com.jonasgerdes.stoppelmap.news.data.repository

import com.jonasgerdes.stoppelmap.news.data.entity.Article
import kotlinx.coroutines.channels.ReceiveChannel

interface NewsRepository {

    suspend fun getNews(): ReceiveChannel<List<Article>>
    suspend fun loadNextPage(): LoadPageResult
    suspend fun refresh(clearOld: Boolean): LoadPageResult


    sealed class LoadPageResult {
        object Success : LoadPageResult()
        object Error : LoadPageResult()
        object NetworkError : LoadPageResult()
    }
}