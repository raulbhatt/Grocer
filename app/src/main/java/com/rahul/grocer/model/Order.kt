package com.rahul.grocer.model

import java.time.LocalDateTime
import java.util.UUID

enum class OrderStatus {
    DELIVERED, PROCESSING, CANCELLED
}

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val items: List<CartItem>,
    val totalAmount: Double,
    val date: LocalDateTime = LocalDateTime.now(),
    val status: OrderStatus = OrderStatus.DELIVERED
)
