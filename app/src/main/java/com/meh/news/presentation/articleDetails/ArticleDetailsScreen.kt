package com.meh.news.presentation.articleDetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.meh.news.R
import com.meh.news.presentation.theme.NewsTheme

@Composable
fun ArticleDetailsScreen(backStackEntry: NavBackStackEntry) {
    val viewModel = hiltViewModel<ArticleDetailsViewModel>(backStackEntry)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiAction
            .collect {
                handleUiActions(
                    context = context,
                    uiAction = it
                )
            }
    }

    NewsTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (val value = uiState.value) {
                    ArticleDetailsViewModel.UiState.Loading -> LoadingState()
                    ArticleDetailsViewModel.UiState.Error -> ErrorState()
                    is ArticleDetailsViewModel.UiState.Success -> {
                        SuccessState(
                            uiState = value,
                            onOpenArticle = viewModel::onOpenArticle
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessState(
    uiState: ArticleDetailsViewModel.UiState.Success,
    onOpenArticle: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiState.article.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground),
                fallback = painterResource(R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9)
            )

            Text(
                text = uiState.article.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
            )

            Text(
                text = uiState.article.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(16.dp)

            )
        }

        if (uiState.article.canOpenArticle) {
            Button(
                onClick = { onOpenArticle(uiState.article.articleUrl) },
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.open_article))
            }
        }
    }
}

@Composable
private fun ErrorState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.failed_to_load_article))
    }
}

private fun handleUiActions(
    context: Context,
    uiAction: ArticleDetailsViewModel.UiAction
) {
    when (uiAction) {
        is ArticleDetailsViewModel.UiAction.OpenLink -> {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uiAction.link))
            context.startActivity(browserIntent)
        }
    }
}