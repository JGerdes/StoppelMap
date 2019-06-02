package com.jonasgerdes.stoppelmap.news.view

import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.news.usecase.GetNewsUseCase
import com.jonasgerdes.stoppelmap.news.usecase.LoadMoreNewsUseCase

class NewsViewModel(
    private val getNews: GetNewsUseCase,
    private val loadMoreNews: LoadMoreNewsUseCase
) : ViewModel() {

    
}