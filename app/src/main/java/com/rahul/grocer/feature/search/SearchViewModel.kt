package com.rahul.grocer.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.rahul.grocer.data.Category
import com.rahul.grocer.data.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _categories = MutableStateFlow(CategoryRepository.categories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isEmpty()) {
            _categories.value = CategoryRepository.categories
        } else {
            _categories.value = CategoryRepository.categories.filter {
                it.name.contains(newQuery, ignoreCase = true)
            }
        }
    }
}
