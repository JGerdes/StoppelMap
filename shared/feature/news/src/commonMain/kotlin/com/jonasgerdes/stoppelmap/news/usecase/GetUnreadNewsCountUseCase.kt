package com.jonasgerdes.stoppelmap.news.usecase

import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetUnreadNewsCountUseCase(
    private val newsRepository: NewsRepository
) {

    operator fun invoke(): Flow<Long> = newsRepository.getUnreadArticleCount()
}