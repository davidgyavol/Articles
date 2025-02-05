package com.meh.news.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meh.news.presentation.navigation.Destination
import com.meh.news.presentation.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MainViewModel @Inject constructor(
    navigationManager: NavigationManager
) : ViewModel() {

    private val _uiAction = MutableSharedFlow<UiAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    init {
        navigationManager
            .commands
            .onEach { _uiAction.emit(UiAction.Navigate(it)) }
            .launchIn(viewModelScope)
    }

    sealed interface UiAction {
        data class Navigate(val destination: Destination) : UiAction
    }
}