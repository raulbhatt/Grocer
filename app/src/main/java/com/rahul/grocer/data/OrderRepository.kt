package com.rahul.grocer.data

import com.rahul.grocer.model.CartItem
import com.rahul.grocer.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

object OrderRepository {
    private val _pastOrders = MutableStateFlow<List<Order>>(emptyList())
    val pastOrders: StateFlow<List<Order>> = _pastOrders.asStateFlow()

    init {
        // Add some mock past orders
        val mockProducts = MockData.getProducts().take(5)
        val mockOrder1 = Order(
            items = mockProducts.take(3).map { CartItem(it, quantity = 1) },
            totalAmount = mockProducts.take(3).sumOf { it.price },
            date = LocalDateTime.now().minusDays(5)
        )
        val mockOrder2 = Order(
            items = mockProducts.takeLast(2).map { CartItem(it, quantity = 2) },
            totalAmount = mockProducts.takeLast(2).sumOf { it.price * 2 },
            date = LocalDateTime.now().minusDays(12)
        )
        _pastOrders.value = listOf(mockOrder1, mockOrder2)
    }

    fun placeOrder(items: List<CartItem>, totalAmount: Double) {
        val newOrder = Order(
            items = items,
            totalAmount = totalAmount
        )
        _pastOrders.update { currentOrders ->
            listOf(newOrder) + currentOrders
        }
    }
}
