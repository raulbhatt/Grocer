package com.rahul.grocer.data

import com.rahul.grocer.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object WishlistRepository {
    private val _wishlistItems = MutableStateFlow<List<Product>>(emptyList())
    val wishlistItems: StateFlow<List<Product>> = _wishlistItems.asStateFlow()

    fun toggleWishlist(product: Product) {
        _wishlistItems.update { currentItems ->
            if (currentItems.any { it.id == product.id }) {
                currentItems.filter { it.id != product.id }
            } else {
                currentItems + product
            }
        }
    }

    fun isInWishlist(product: Product): Boolean {
        return _wishlistItems.value.any { it.id == product.id }
    }
}
