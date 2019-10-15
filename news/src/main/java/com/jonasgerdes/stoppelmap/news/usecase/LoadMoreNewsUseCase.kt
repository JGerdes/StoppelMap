package com.jonasgerdes.stoppelmap.news.usecase

import com.jonasgerdes.stoppelmap.news.data.entity.Result
import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository

class LoadMoreNewsUseCase(
    private val usecaseRepository: NewsRepository
) {

    suspend operator fun invoke(): Result = when (val result = usecaseRepository.loadNextPage()) {
        NewsRepository.LoadPageResult.Success -> Result.Success
        NewsRepository.LoadPageResult.Error -> Result.Error
        NewsRepository.LoadPageResult.NetworkError -> Result.NetworkError
        NewsRepository.LoadPageResult.NoNextPage -> Result.Success
    }
}