package com.rahul.grocer.model

enum class StockStatus {
    IN_STOCK, LOW_STOCK, OUT_OF_STOCK
}

enum class ProductCategory {
    FRUITS, VEGETABLES, DAIRY, BAKERY, SNACKS, BEVERAGES, MEAT, PANTRY
}

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double?,
    val discountPercent: Int,
    val rating: Double,
    val reviewCount: Int,
    val imageUrl: String,
    val calories: Int,
    val expiryDate: String,
    val stockStatus: StockStatus,
    val category: ProductCategory
) {
    val formattedPrice: String
        get() = "$${String.format("%.2f", price)}"
        
    val formattedOriginalPrice: String?
        get() = originalPrice?.let { "$${String.format("%.2f", it)}" }
}
