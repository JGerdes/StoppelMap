package com.jonasgerdes.stoppelmap.news.ui

import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewsDependencies : KoinComponent {
    val newsRepository: NewsRepository by inject()
}