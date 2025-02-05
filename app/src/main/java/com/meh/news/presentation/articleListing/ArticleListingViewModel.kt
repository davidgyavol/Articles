package com.meh.news.presentation.articleListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meh.news.domain.usecase.GetHeadlinesUseCase
import com.meh.news.presentation.navigation.Destination
import com.meh.news.presentation.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ArticleListingViewModel @Inject constructor(
    private val getHeadlinesUseCase: GetHeadlinesUseCase,
    private val navigationManager: NavigationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    fun onArticleClick(id: String) {
        navigationManager.navigate(
            Destination.ArticleDetails(id)
        )
    }

    fun onRetryClick() {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = getHeadlinesUseCase.getTopHeadlines()
                .map { articles ->
                    UiState.Success(
                        articles.map {
                            UiState.Success.Article(
                                id = it.id,
                                title = it.title,
                                imageUrl = it.imageUrl
                            )
                        }
                    )
                }
                .getOrDefault(UiState.Error)
        }
    }

    sealed interface UiState {
        data class Success(
            val articles: List<Article>
        ) : UiState {
            data class Article(
                val id: String,
                val title: String,
                val imageUrl: String?
            )
        }

        data object Loading : UiState

        data object Error : UiState
    }
}
