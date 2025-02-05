package com.meh.news.data.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    val title: String,
    val description: String?,
    val url: String?,
    val urlToImage: String?
)
