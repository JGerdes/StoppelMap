package com.jonasgerdes.stoppelmap.news.usecase

import com.jonasgerdes.stoppelmap.news.data.NewsRepository

class LoadLatestNewsUseCase(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke() {
        newsRepository.loadLatestArticles()
    }
}