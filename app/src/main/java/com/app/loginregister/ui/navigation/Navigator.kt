package com.app.loginregister.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 */
class Navigator(start: NavKey) {

    private val _backStack = mutableStateListOf(start)
    val backStack: List<NavKey> = _backStack

    fun navigate(route: NavKey) {
        _backStack.add(route)
    }

    fun goBack() {
        if (_backStack.size > 1) {
            _backStack.removeAt(_backStack.size - 1)
        }
    }

    fun replaceAll(route: NavKey) {
        _backStack.clear()
        _backStack.add(route)
    }
}
