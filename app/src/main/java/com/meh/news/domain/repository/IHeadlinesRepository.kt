package com.meh.news.domain.repository

import com.meh.news.domain.model.Article

interface IHeadlinesRepository {

    suspend fun getTopHeadlines(): List<Article>

    suspend fun getArticle(id: String): Article?
}