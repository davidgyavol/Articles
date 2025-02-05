package com.meh.news.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meh.news.presentation.articleDetails.ArticleDetailsScreen
import com.meh.news.presentation.articleListing.NewsListingScreen
import com.meh.news.presentation.navigation.Destination
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                viewModel.uiAction
                    .onEach {
                        when (it) {
                            is MainViewModel.UiAction.Navigate -> navController.navigate(it.destination)
                        }
                    }
                    .launchIn(this)
            }

            NavHost(navController = navController, startDestination = Destination.ArticleListing) {
                composable<Destination.ArticleListing> { NewsListingScreen() }
                composable<Destination.ArticleDetails> { backStackEntry ->
                    ArticleDetailsScreen(backStackEntry)
                }
            }
        }
    }
}