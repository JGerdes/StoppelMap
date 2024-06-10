package com.jonasgerdes.stoppelmap.news.ui

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.model.Article
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmm.viewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepository: NewsRepository
) : KMMViewModel() {

    private val articles = newsRepository.getArticles()

    fun onListEndReached() {
        viewModelScope.coroutineScope.launch {
            newsRepository.loadMoreArticles()
        }
    }

    val state: StateFlow<ViewState> =
        articles
            .map(::ViewState)
            .stateIn(
                viewModelScope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ViewState()
            )

    data class ViewState
    @DefaultArgumentInterop.Enabled
    constructor(
        val articles: List<Article> = emptyList()
    )

}
