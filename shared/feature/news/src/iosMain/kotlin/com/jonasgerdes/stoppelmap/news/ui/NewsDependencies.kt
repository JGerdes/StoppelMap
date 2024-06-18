package com.jonasgerdes.stoppelmap.news.ui

import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.usecase.GetUnreadNewsCountUseCase
import com.jonasgerdes.stoppelmap.news.usecase.LoadLatestNewsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewsDependencies : KoinComponent {
    val newsRepository: NewsRepository by inject()
    val loadLatestNews: LoadLatestNewsUseCase by inject()
    val getUnreadNewsCount: GetUnreadNewsCountUseCase by inject()
}