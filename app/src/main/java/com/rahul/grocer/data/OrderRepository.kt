package com.rahul.grocer.data

import com.rahul.grocer.model.CartItem
import com.rahul.grocer.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor() {
    private val _pastOrders = MutableStateFlow<List<Order>>(emptyList())
    val pastOrders: StateFlow<List<Order>> = _pastOrders.asStateFlow()

    init {
        // Mock data removed for initial empty view
        _pastOrders.value = emptyList()
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
