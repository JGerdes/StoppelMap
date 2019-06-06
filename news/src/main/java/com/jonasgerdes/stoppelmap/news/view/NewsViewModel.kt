package com.jonasgerdes.stoppelmap.news.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.jonasgerdes.stoppelmap.news.data.entity.Result
import com.jonasgerdes.stoppelmap.news.usecase.GetNewsUseCase
import com.jonasgerdes.stoppelmap.news.usecase.LoadMoreNewsUseCase
import com.jonasgerdes.stoppelmap.news.usecase.RefreshNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

@UseExperimental(ObsoleteCoroutinesApi::class)
class NewsViewModel(
    private val getNews: GetNewsUseCase,
    private val loadMoreNews: LoadMoreNewsUseCase,
    private val refreshNews: RefreshNewsUseCase
) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>(emptyList())
    val articles: LiveData<List<Article>> get() = _articles

    private val _loadingState = MutableLiveData<LoadingState>(LoadingState.Idle)
    val loadingState: LiveData<LoadingState> get() = _loadingState

    private var isLoading = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getNews().consumeEach {
                _articles.postValue(it)
            }
        }

        refresh(clear = false)
    }

    fun loadMoreArticles() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(LoadingState.Loading.More)
            _loadingState.postValue(
                when (loadMoreNews()) {
                    Result.Success -> LoadingState.Idle
                    Result.Error -> LoadingState.Error.Unknown
                    Result.NetworkError -> LoadingState.Error.NoNetwork
                }
            )
            isLoading = false
        }
    }

    fun refreshArticles() = refresh(clear = true)

    private fun refresh(clear: Boolean = false) {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(LoadingState.Loading.Refresh)
            _loadingState.postValue(
                when (refreshNews(clear)) {
                    Result.Success -> LoadingState.Idle
                    Result.Error -> LoadingState.Error.Unknown
                    Result.NetworkError -> LoadingState.Error.NoNetwork
                }
            )
            isLoading = false
        }
    }
}