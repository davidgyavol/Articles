package com.meh.news.presentation.navigation

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Singleton
class NavigationManager @Inject constructor() {

    private val _commands = MutableSharedFlow<Destination>(extraBufferCapacity = 1)
    val commands = _commands.asSharedFlow()

    fun navigate(
        command: Destination
    ) {
        _commands.tryEmit(command)
    }
}