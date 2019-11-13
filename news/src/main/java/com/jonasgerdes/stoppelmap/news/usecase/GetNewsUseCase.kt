package com.jonasgerdes.stoppelmap.news.usecase

import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val usecaseRepository: NewsRepository
) {

    suspend operator fun invoke() = usecaseRepository.getNews()
}