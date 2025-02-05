package com.meh.news.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object ArticleListing : Destination

    @Serializable
    data class ArticleDetails(val id: String) : Destination

}