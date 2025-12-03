package com.rahul.grocer.feature.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rahul.grocer.data.CartRepository
import com.rahul.grocer.data.OrderRepository
import com.rahul.grocer.data.WishlistRepository
import com.rahul.grocer.model.CartItem
import com.rahul.grocer.model.Product
import com.rahul.grocer.model.SubstitutionPolicy
import com.rahul.grocer.ui.components.OrbitButton
import com.rahul.grocer.ui.components.OrbitGlassCard
import com.rahul.grocer.ui.theme.StarlightSilver
import com.rahul.grocer.feature.payment.PaymentSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CartScreen() {
    val cartItems by CartRepository.cartItems.collectAsState()
    val appliedPromoCode by CartRepository.appliedPromoCode.collectAsState()
    val pastOrders by OrderRepository.pastOrders.collectAsState()
    val cartTotals by CartRepository.cartTotals.collectAsState()
    
    var isCheckingOut by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val liftOffY by animateFloatAsState(targetValue = if (isCheckingOut) -1000f else 0f, label = "LiftOff")

    // Calculate totals
    val subTotal = cartTotals.subTotal
    val discount = cartTotals.discount
    val total = cartTotals.total

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .offset(y = liftOffY.dp)
        ) {
            Text(text = "Cargo Bay", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cart Items
                items(cartItems) { item ->
                    CartItemRow(item)
                }

                // Promo Code Section
                item {
                    PromoCodeSection(
                        appliedCode = appliedPromoCode?.code,
                        onApply = { CartRepository.applyPromoCode(it) },
                        onRemove = { CartRepository.removePromoCode() }
                    )
                }

                // Bill Breakdown
                item {
                    BillBreakdown(subTotal, discount, total)
                }

                // Buy Again Section
                if (pastOrders.isNotEmpty()) {
                    item {
                        Text(
                            text = "Buy Again",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Flatten all items from past orders and take unique products
                            val recentProducts = pastOrders.flatMap { it.items }.map { it.product }.distinctBy { it.id }.take(5)
                            items(recentProducts) { product ->
                                BuyAgainItem(product)
                            }
                        }
                    }
                }
                
                // Bottom Spacer
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        // Checkout Button (Fixed at bottom)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .offset(y = liftOffY.dp)
        ) {
            OrbitButton(
                text = "Initiate Lift Off • $${String.format("%.2f", total)}",
                onClick = { 
                    showPaymentSheet = true
                }
            )
        }
        
        PaymentSheet(
            isVisible = showPaymentSheet,
            totalAmount = total,
            onDismiss = { showPaymentSheet = false },
            onPaymentSuccess = {
                showPaymentSheet = false
                isCheckingOut = true
                scope.launch {
                    // Simulate order placement after animation
                    OrderRepository.placeOrder(cartItems, total)
                    CartRepository.clearCart()
                    delay(1000) // Wait for lift off animation
                    isCheckingOut = false // Reset UI to original position
                }
            }
        )
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    var showSubstitutionOptions by remember { mutableStateOf(false) }

    OrbitGlassCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product Image
                AsyncImage(
                    model = item.product.imageUrl,
                    contentDescription = item.product.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.product.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text(
                        text = "$${String.format("%.2f", item.product.price)}", 
                        style = MaterialTheme.typography.bodyMedium, 
                        color = StarlightSilver
                    )
                    
                    // Substitution Policy
                    Row(
                        modifier = Modifier
                            .clickable { showSubstitutionOptions = !showSubstitutionOptions }
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sub: ${item.substitutionPolicy.name.replace("_", " ")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = StarlightSilver
                        )
                        Icon(
                            imageVector = if (showSubstitutionOptions) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Substitution Options",
                            tint = StarlightSilver,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                // Quantity Controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { CartRepository.updateQuantity(item, item.quantity - 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Decrease", tint = Color.White)
                    }
                    
                    Text(
                        text = "${item.quantity}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    
                    IconButton(
                        onClick = { CartRepository.updateQuantity(item, item.quantity + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
                    }
                }
            }

            // Substitution Dropdown (Simple expansion)
            AnimatedVisibility(visible = showSubstitutionOptions) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    SubstitutionPolicy.values().forEach { policy ->
                        Text(
                            text = policy.name.replace("_", " "),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (item.substitutionPolicy == policy) Color.Green else Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    CartRepository.updateSubstitutionPolicy(item, policy)
                                    showSubstitutionOptions = false
                                }
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PromoCodeSection(
    appliedCode: String?,
    onApply: (String) -> Boolean,
    onRemove: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    OrbitGlassCard {
        Column {
            Text(text = "Promo Code", style = MaterialTheme.typography.titleSmall, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            
            if (appliedCode != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x3300FF00), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Applied: $appliedCode", color = Color.Green)
                    IconButton(onClick = { 
                        onRemove()
                        code = ""
                    }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Green)
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { 
                            code = it
                            error = null
                        },
                        placeholder = { Text("Enter code", color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = StarlightSilver
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { 
                            if (onApply(code)) {
                                error = null
                            } else {
                                error = "Invalid code"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
                    ) {
                        Text("Apply")
                    }
                }
                if (error != null) {
                    Text(text = error!!, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
fun BillBreakdown(subTotal: Double, discount: Double, total: Double) {
    OrbitGlassCard {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Subtotal", color = StarlightSilver)
                Text(text = "$${String.format("%.2f", subTotal)}", color = Color.White)
            }
            if (discount > 0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Discount", color = Color.Green)
                    Text(text = "-$${String.format("%.2f", discount)}", color = Color.Green)
                }
            }
            Divider(color = Color.White.copy(alpha = 0.2f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text(text = "$${String.format("%.2f", total)}", style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
        }
    }
}

@Composable
fun BuyAgainItem(product: Product) {
    val isInWishlist by WishlistRepository.wishlistItems.collectAsState()
    val isFavorite = isInWishlist.any { it.id == product.id }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { CartRepository.addToCart(product) },
                contentScale = ContentScale.Crop
            )
            // Wishlist Heart
            IconButton(
                onClick = { WishlistRepository.toggleWishlist(product) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Wishlist",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = product.name,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "$${String.format("%.2f", product.price)}",
            style = MaterialTheme.typography.labelSmall,
            color = StarlightSilver
        )
    }
}
