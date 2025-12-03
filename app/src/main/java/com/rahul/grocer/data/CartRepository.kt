package com.rahul.grocer.data

import com.rahul.grocer.model.CartItem
import com.rahul.grocer.model.CartTotals
import com.rahul.grocer.model.Product
import com.rahul.grocer.model.PromoCode
import com.rahul.grocer.model.SubstitutionPolicy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _appliedPromoCode = MutableStateFlow<PromoCode?>(null)
    val appliedPromoCode: StateFlow<PromoCode?> = _appliedPromoCode.asStateFlow()



    // Available promo codes (Mock)
    private val availablePromoCodes = listOf(
        PromoCode("SAVE10", 10, "10% off your order"),
        PromoCode("FREESHIP", 0, "Free Shipping (Not implemented yet)"), // Just for demo
        PromoCode("ORBIT20", 20, "20% off for Orbit members")
    )

    private val _cartTotals = MutableStateFlow(CartTotals())
    val cartTotals: StateFlow<CartTotals> = _cartTotals.asStateFlow()

    private fun updateTotals() {
        val items = _cartItems.value
        val promo = _appliedPromoCode.value
        
        val subTotal = items.sumOf { it.totalDiscountedPrice }
        val discount = if (promo != null) {
            subTotal * (promo.discountPercent / 100.0)
        } else {
            0.0
        }
        val total = subTotal - discount
        
        _cartTotals.value = CartTotals(subTotal, discount, total)
    }

    fun addToCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + CartItem(product)
            }
        }
        updateTotals()
    }

    fun removeFromCart(cartItem: CartItem) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.product.id != cartItem.product.id }
        }
        updateTotals()
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(cartItem)
            return
        }
        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.product.id == cartItem.product.id) it.copy(quantity = newQuantity) else it
            }
        }
        updateTotals()
    }

    fun updateSubstitutionPolicy(cartItem: CartItem, policy: SubstitutionPolicy) {
        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.product.id == cartItem.product.id) it.copy(substitutionPolicy = policy) else it
            }
        }
        // No total update needed for policy change
    }

    fun applyPromoCode(code: String): Boolean {
        val promo = availablePromoCodes.find { it.code.equals(code, ignoreCase = true) }
        if (promo != null) {
            _appliedPromoCode.value = promo
            updateTotals()
            return true
        }
        return false
    }

    fun removePromoCode() {
        _appliedPromoCode.value = null
        updateTotals()
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedPromoCode.value = null
        updateTotals()
    }
}
