package com.jonasgerdes.stoppelmap.news.usecase

import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository

class LoadMoreNewsUseCase(
    private val usecaseRepository: NewsRepository
) {

    suspend operator fun invoke(): Result = when (val result = usecaseRepository.loadNextPage()) {
        NewsRepository.LoadPageResult.Success -> Result.Success
        NewsRepository.LoadPageResult.Error -> Result.Error
        NewsRepository.LoadPageResult.NetworkError -> Result.NetworkError
    }


    sealed class Result {
        object Success : Result()
        object Error : Result()
        object NetworkError : Result()
    }
}