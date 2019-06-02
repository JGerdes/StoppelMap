package com.jonasgerdes.stoppelmap.news.usecase

import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository

class GetNewsUseCase(
    private val usecaseRepository: NewsRepository
) {

    operator fun invoke() = usecaseRepository.getNews()
}