package com.jonasgerdes.stoppelmap.news.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.jonasgerdes.stoppelmap.news.usecase.GetNewsUseCase
import com.jonasgerdes.stoppelmap.news.usecase.LoadMoreNewsUseCase
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@ObsoleteCoroutinesApi
class NewsViewModel(
    private val getNews: GetNewsUseCase,
    private val loadMoreNews: LoadMoreNewsUseCase
) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>(emptyList())
    val articles: LiveData<List<Article>> get() = _articles

    private val _loadingState = MutableLiveData<LoadingState>(LoadingState.Idle)
    val loadingState: LiveData<LoadingState> get() = _loadingState

    init {
        viewModelScope.launch {
            getNews().consumeEach {
                _articles.postValue(it)
            }
        }
    }

    fun loadMoreArticles() {
        viewModelScope.launch {
            _loadingState.postValue(LoadingState.Loading)
            _loadingState.postValue(
                when (loadMoreNews()) {
                    LoadMoreNewsUseCase.Result.Success -> LoadingState.Idle
                    LoadMoreNewsUseCase.Result.Error -> LoadingState.Error.Unknown
                    LoadMoreNewsUseCase.Result.NetworkError -> LoadingState.Error.NoNetwork
                }
            )
        }
    }
}