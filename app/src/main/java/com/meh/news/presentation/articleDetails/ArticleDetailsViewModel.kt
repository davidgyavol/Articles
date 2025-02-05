package com.meh.news.presentation.articleDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.meh.news.domain.usecase.GetArticleUseCase
import com.meh.news.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getArticleUseCase: GetArticleUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<UiAction>(extraBufferCapacity = 1)
    val uiAction = _uiAction.asSharedFlow()

    init {
        viewModelScope.launch {
            val articleId = savedStateHandle.toRoute<Destination.ArticleDetails>().id

            getArticleUseCase.getArticle(articleId)
                .map { article ->
                    article?.let {
                        UiState.Success(
                            UiState.Success.Article(
                                title = it.title,
                                description = it.description,
                                canOpenArticle = it.articleUrl.isNotBlank(),
                                articleUrl = it.articleUrl,
                                imageUrl = it.imageUrl
                            )
                        )
                    } ?: UiState.Error
                }
                .getOrDefault(UiState.Error)
                .let {
                    _uiState.value = it
                }
        }
    }

    fun onOpenArticle(url: String) {
        _uiAction.tryEmit(UiAction.OpenLink(url))
    }

    sealed interface UiState {
        data class Success(
            val article: Article
        ) : UiState {
            data class Article(
                val title: String,
                val description: String,
                val imageUrl: String?,
                val canOpenArticle: Boolean,
                val articleUrl: String
            )
        }

        data object Loading : UiState

        data object Error : UiState
    }

    sealed interface UiAction {
        data class OpenLink(val link: String) : UiAction
    }
}
