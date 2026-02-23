package com.rahul.grocer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.grocer.data.MockData
import com.rahul.grocer.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Artificial delay to show the cool Lottie animation
            kotlinx.coroutines.delay(2000) 
            
            val allProducts = MockData.getProducts()
            _uiState.value = HomeUiState(
                isLoading = false,
                greeting = "Good Evening, Alex",
                smartListItems = allProducts.take(10), // First 10 for smart list
                expressItems = allProducts.drop(10).take(5), // Next 5 for express
                allProducts = allProducts // Full list for dashboard
            )
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val greeting: String = "Loading...",
    val smartListItems: List<Product> = emptyList(),
    val expressItems: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList()
)
