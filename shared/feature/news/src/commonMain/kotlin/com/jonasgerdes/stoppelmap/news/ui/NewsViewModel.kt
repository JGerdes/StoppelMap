package com.jonasgerdes.stoppelmap.news.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.model.Article
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepository: NewsRepository
) : KMMViewModel() {

    private val articles = newsRepository.getArticles()
    private val isRefreshing = MutableStateFlow(false)
    private val isLoadingMore = MutableStateFlow(false)

    fun onListEndReached() {
        viewModelScope.coroutineScope.launch {
            isLoadingMore.value = true
            newsRepository.loadMoreArticles()
            isLoadingMore.value = false
        }
    }

    suspend fun forceRefresh() {
        newsRepository.forceRefresh()
    }

    fun onShowFirstArticle() {
        viewModelScope.coroutineScope.launch {
            newsRepository.markAllArticlesRead()
        }
    }

    val state: StateFlow<ViewState> =
        combine(
            articles,
            isRefreshing,
            isLoadingMore,
            ::ViewState
        )
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val articles: List<Article> = emptyList(),
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
    )

}
