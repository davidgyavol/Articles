package com.meh.news.data.networking.api

import com.meh.news.data.networking.model.ArticlesResponse
import retrofit2.http.GET

interface HeadlinesApi {

    @GET("top-headlines?country=us")
    suspend fun getTopHeadlines(): ArticlesResponse
}