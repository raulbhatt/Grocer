package com.rahul.grocer.model

enum class SubstitutionPolicy {
    BEST_MATCH, SAME_BRAND, NO_SUBSTITUTION
}

data class CartItem(
    val product: Product,
    var quantity: Int = 1,
    var substitutionPolicy: SubstitutionPolicy = SubstitutionPolicy.BEST_MATCH
) {
    val totalPrice: Double
        get() = (product.originalPrice ?: product.price) * quantity
        
    val totalDiscountedPrice: Double
        get() = product.price * quantity
}
