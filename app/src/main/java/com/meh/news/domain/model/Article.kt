package com.meh.news.domain.model

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val articleUrl: String,
    val imageUrl: String
)