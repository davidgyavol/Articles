package com.meh.news.domain.usecase

import com.meh.news.data.di.IoDispatcher
import com.meh.news.data.helpers.runSuspendCatching
import com.meh.news.domain.repository.IHeadlinesRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetArticleUseCase @Inject constructor(
    private val headlinesRepository: IHeadlinesRepository,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getArticle(id: String) = runSuspendCatching {
        withContext(ioDispatcher) {
            headlinesRepository.getArticle(id = id)
        }
    }
}