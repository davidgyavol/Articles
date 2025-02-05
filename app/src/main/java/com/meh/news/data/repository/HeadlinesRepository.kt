package com.meh.news.data.repository

import com.meh.news.data.networking.api.HeadlinesApi
import com.meh.news.domain.model.Article
import com.meh.news.domain.repository.IHeadlinesRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class HeadlinesRepository @Inject constructor(
    private val headlinesApi: HeadlinesApi
) : IHeadlinesRepository {

    private val headlinesMutex = Mutex()
    private var headlines = listOf<Article>()

    override suspend fun getTopHeadlines(): List<Article> {
        val headlines = headlinesApi.getTopHeadlines()
            .articles
            .map {
                Article(
                    id = UUID.randomUUID().toString(),
                    title = it.title,
                    description = it.description ?: "",
                    articleUrl = it.url ?: "",
                    imageUrl = it.urlToImage ?: ""
                )
            }

        headlinesMutex.withLock {
            this.headlines = headlines
        }

        return headlinesMutex.withLock { this.headlines }
    }

    override suspend fun getArticle(id: String): Article? {
        return headlinesMutex.withLock {
            this.headlines.find { it.id == id }
        }
    }
}