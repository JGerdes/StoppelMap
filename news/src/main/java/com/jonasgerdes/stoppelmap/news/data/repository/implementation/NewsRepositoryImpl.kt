package com.jonasgerdes.stoppelmap.news.data.repository.implementation

import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository.LoadPageResult
import com.jonasgerdes.stoppelmap.news.data.source.remote.NetworkResult
import com.jonasgerdes.stoppelmap.news.data.source.remote.RemoteNewsSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel


class NewsRepositoryImpl(
    private val remoteSource: RemoteNewsSource
) : NewsRepository {

    private val newsMemCache = mutableListOf<Article>()

    private val newsChannel = Channel<List<Article>>()

    override fun getNews(): ReceiveChannel<List<Article>> = newsChannel

    override suspend fun loadNextPage(): NewsRepository.LoadPageResult {
        return when (val result = remoteSource.loadNextPage()) {
            is NetworkResult.Success -> {
                newsMemCache.addAll(result.data.articles.asArticles())
                newsChannel.send(newsMemCache)
                LoadPageResult.Success
            }
            is NetworkResult.ServerError -> if (result.code == 404) LoadPageResult.Success else LoadPageResult.Error
            NetworkResult.NetworkError -> LoadPageResult.NetworkError
        }
    }
}



