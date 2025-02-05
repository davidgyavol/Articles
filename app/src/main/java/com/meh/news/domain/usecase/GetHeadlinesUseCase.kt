package com.meh.news.domain.usecase

import com.meh.news.data.di.IoDispatcher
import com.meh.news.data.helpers.runSuspendCatching
import com.meh.news.domain.repository.IHeadlinesRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetHeadlinesUseCase @Inject constructor(
    private val headlinesRepository: IHeadlinesRepository,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getTopHeadlines() = runSuspendCatching {
        withContext(ioDispatcher) {
            headlinesRepository.getTopHeadlines()
        }
    }
}