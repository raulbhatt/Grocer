package com.rahul.grocer.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.grocer.data.CartRepository
import com.rahul.grocer.data.ThemeRepository
import com.rahul.grocer.ui.components.AddToCartAnimation
import com.rahul.grocer.ui.components.OrbitGlassCard
import com.rahul.grocer.ui.components.OrbitLogo
import com.rahul.grocer.ui.components.ProductCard
import com.rahul.grocer.ui.theme.FreshGreen
import com.rahul.grocer.ui.theme.NebulaPurple
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToTracker: () -> Unit = {},
    onNavigateToZeroWaste: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddAnimation by remember { mutableStateOf(false) }
    var showLoadingIndicator by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        showLoadingIndicator = false
    }

    val onAddToCart = { product: com.rahul.grocer.model.Product ->
        CartRepository.addToCart(product)
        showAddAnimation = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp) // Space for bottom nav
        ) {
            // Header
            item {
                HomeHeader(greeting = uiState.greeting, onNavigateToSupport = onNavigateToSupport)
            }

            // Smart Lists
            item {
                SectionTitle(title = "Smart Reorder", subtitle = "Running low based on your history")
            }

            val smartListChunked = uiState.smartListItems.chunked(2)
            items(smartListChunked) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (product in rowItems) {
                        ProductCard(
                            name = product.name,
                            price = product.formattedPrice,
                            imageUrl = product.imageUrl,
                            discountPercent = product.discountPercent,
                            originalPrice = product.formattedOriginalPrice,
                            stockStatus = product.stockStatus,
                            modifier = Modifier.weight(1f),
                            onAdd = { onAddToCart(product) }
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Express Lane Banner
            item {
                Spacer(modifier = Modifier.height(32.dp))
                ExpressLaneBanner(onClick = onNavigateToTracker)
            }

            // Zero Waste
            item {
                Spacer(modifier = Modifier.height(32.dp))
                ZeroWasteCard(onClick = onNavigateToZeroWaste)
            }

            // Browse All (Grid)
            item {
                SectionTitle(title = "Browse All", subtitle = "Explore our fresh collection")
            }

            val chunkedItems = uiState.allProducts.chunked(2)
            items(chunkedItems) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (product in rowItems) {
                        ProductCard(
                            name = product.name,
                            price = product.formattedPrice,
                            imageUrl = product.imageUrl,
                            discountPercent = product.discountPercent,
                            originalPrice = product.formattedOriginalPrice,
                            stockStatus = product.stockStatus,
                            modifier = Modifier.weight(1f),
                            onAdd = { onAddToCart(product) }
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        AddToCartAnimation(
            visible = showAddAnimation,
            onAnimationEnd = { showAddAnimation = false }
        )

        AnimatedVisibility(
            visible = uiState.isLoading && showLoadingIndicator,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                    .clickable(enabled = false, onClick = {}),
                contentAlignment = Alignment.Center
            ) {
                OrbitLogo(size = 80.dp, isAnimated = true)
            }
        }
    }
}

@Composable
fun HomeHeader(greeting: String, onNavigateToSupport: () -> Unit = {}) {
    val isDarkTheme by ThemeRepository.isDarkTheme.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "It's dinner time. How about Pasta?",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }

        Row {
            IconButton(onClick = onNavigateToSupport) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Support",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { ThemeRepository.toggleTheme() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun ExpressLaneBanner(onClick: () -> Unit) {
    OrbitGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Build, contentDescription = null, tint = NebulaPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Express Lane",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
                Text(
                    text = "Delivery in 15 mins",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ZeroWasteCard(onClick: () -> Unit) {
    OrbitGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Face, contentDescription = null, tint = FreshGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Zero-Waste Fridge",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
                Text(
                    text = "3 items expiring soon. See recipes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
