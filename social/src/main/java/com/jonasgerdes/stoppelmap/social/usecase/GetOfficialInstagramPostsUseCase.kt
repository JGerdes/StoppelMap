package com.jonasgerdes.stoppelmap.social.usecase

import com.jonasgerdes.stoppelmap.social.data.repository.PostsRepository
import kotlinx.coroutines.channels.map


class GetOfficialInstagramPostsUseCase(
    private val profilePostsRepository: PostsRepository
) {

    suspend operator fun invoke() = profilePostsRepository.getPosts()
        .map {posts ->
            posts.filter { it.description?.contains("#stoppelmarkt") ?: false }
        }

    sealed class Result {
        object Success : Result()
        object Error : Result()
        object NetworkError : Result()
    }

    suspend fun loadMore() = profilePostsRepository.loadNextPage()

    suspend fun refresh(clearOld: Boolean): Result =
        when (val result = profilePostsRepository.refresh(clearOld)) {
            PostsRepository.LoadPageResult.Success -> Result.Success
            PostsRepository.LoadPageResult.Error -> Result.Error
            PostsRepository.LoadPageResult.NetworkError -> Result.NetworkError
            PostsRepository.LoadPageResult.NoNextPage -> Result.Success
        }
}