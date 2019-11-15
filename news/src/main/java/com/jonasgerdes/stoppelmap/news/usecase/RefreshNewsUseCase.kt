package com.jonasgerdes.stoppelmap.news.usecase

import com.jonasgerdes.stoppelmap.news.data.entity.Result
import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository
import javax.inject.Inject

class RefreshNewsUseCase @Inject constructor(
    private val usecaseRepository: NewsRepository
) {

    suspend operator fun invoke(clearOld: Boolean): Result = when (val result = usecaseRepository.refresh(clearOld)) {
        NewsRepository.LoadPageResult.Success -> Result.Success
        NewsRepository.LoadPageResult.Error -> Result.Error
        NewsRepository.LoadPageResult.NetworkError -> Result.NetworkError
        NewsRepository.LoadPageResult.NoNextPage -> Result.Success
    }
}