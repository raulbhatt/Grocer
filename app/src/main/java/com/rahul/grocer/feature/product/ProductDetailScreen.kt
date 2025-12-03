package com.rahul.grocer.feature.product

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rahul.grocer.ui.components.OrbitButton
import com.rahul.grocer.ui.theme.StarlightSilver

@Composable
fun ProductDetailScreen() {
    // Mock Data
    val name = "Organic Whole Milk"
    val price = "$4.99"
    val description = "Fresh from local farms. 100% Organic and grass-fed."

    var isAdded by remember { mutableStateOf(false) }
    val offsetY by animateFloatAsState(targetValue = if (isAdded) -100f else 0f, label = "FloatAnimation")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Product Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .offset(y = offsetY.dp) // Simple "float away" animation
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(text = name, style = MaterialTheme.typography.headlineLarge, color = Color.White)
        Text(text = price, style = MaterialTheme.typography.headlineMedium, color = StarlightSilver)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = description, style = MaterialTheme.typography.bodyLarge, color = StarlightSilver)
        
        Spacer(modifier = Modifier.weight(1f))
        
        OrbitButton(
            text = if (isAdded) "Added to Cart" else "Add to Cart",
            onClick = { isAdded = !isAdded }
        )
    }
}
