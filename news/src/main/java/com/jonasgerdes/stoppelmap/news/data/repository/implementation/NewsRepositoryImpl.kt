package com.jonasgerdes.stoppelmap.news.data.repository.implementation

import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository.LoadPageResult
import com.jonasgerdes.stoppelmap.news.data.source.local.LocalNewsSource
import com.jonasgerdes.stoppelmap.news.data.source.remote.Error
import com.jonasgerdes.stoppelmap.news.data.source.remote.NetworkResult
import com.jonasgerdes.stoppelmap.news.data.source.remote.RemoteNewsSource
import kotlinx.coroutines.channels.ReceiveChannel


class NewsRepositoryImpl(
    private val localSource: LocalNewsSource,
    private val remoteSource: RemoteNewsSource
) : NewsRepository {

    override suspend fun getNews(): ReceiveChannel<List<Article>> = localSource.getNewsStream()

    override suspend fun loadNextPage(): NewsRepository.LoadPageResult {
        if (!remoteSource.hasNextPage()) {
            return LoadPageResult.NoNextPage
        }
        return when (val result = remoteSource.loadNextPage()) {
            is NetworkResult.Success -> {
                val articles = result.data.articles.asArticles()
                localSource.persist(articles)
                LoadPageResult.Success
            }
            is NetworkResult.ServerError -> mapServerError(result)
            NetworkResult.NetworkError -> LoadPageResult.NetworkError
        }
    }

    override suspend fun refresh(clearOld: Boolean): LoadPageResult {
        remoteSource.resetToFirstPage()
        return when (val result = remoteSource.loadNextPage()) {
            is NetworkResult.Success -> {
                val articles = result.data.articles.asArticles()
                if (clearOld) localSource.clear()
                localSource.persist(articles)
                LoadPageResult.Success
            }
            is NetworkResult.ServerError -> mapServerError(result)
            NetworkResult.NetworkError -> LoadPageResult.NetworkError
        }
    }

    private fun mapServerError(result: NetworkResult.ServerError<Error>) =
        when (result.code) {
            404 -> LoadPageResult.Error
            else -> LoadPageResult.Error
        }
}



