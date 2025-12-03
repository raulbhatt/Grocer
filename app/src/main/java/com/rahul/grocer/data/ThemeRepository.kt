package com.rahul.grocer.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeRepository {
    private val _isDarkTheme = MutableStateFlow(true) // Default to Dark Mode
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
    
    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
}
