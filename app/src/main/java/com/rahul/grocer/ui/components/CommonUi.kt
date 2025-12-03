package com.rahul.grocer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.rahul.grocer.model.StockStatus
import com.rahul.grocer.ui.theme.CosmicPink
import com.rahul.grocer.ui.theme.GlassBlur
import com.rahul.grocer.ui.theme.GlassWhite
import com.rahul.grocer.ui.theme.NebulaPurple
import com.rahul.grocer.ui.theme.StarlightSilver

@Composable
fun OrbitButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val gradient = Brush.horizontalGradient(listOf(NebulaPurple, CosmicPink))
    
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
        ),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (enabled) gradient else Brush.linearGradient(listOf(Color.Gray, Color.Gray)))
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrbitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = StarlightSilver.copy(alpha = 0.7f)) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = outlinedTextFieldColors(
            focusedBorderColor = NebulaPurple,
            unfocusedBorderColor = StarlightSilver.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = StarlightSilver
        ),
        leadingIcon = leadingIcon
    )
}

@Composable
fun OrbitGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(GlassBlur, RoundedCornerShape(24.dp))
            .border(1.dp, GlassWhite, RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        content()
    }
}

@Composable
fun ProductCard(
    name: String,
    price: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    discountPercent: Int = 0,
    originalPrice: String? = null,
    stockStatus: StockStatus = StockStatus.IN_STOCK,
    onAdd: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(GlassBlur, RoundedCornerShape(16.dp))
            .border(1.dp, GlassWhite, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .width(160.dp) // Slightly wider
    ) {
        Column {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
                
                if (discountPercent > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .background(CosmicPink, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$discountPercent% OFF",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = price,
                    style = MaterialTheme.typography.bodyLarge,
                    color = StarlightSilver,
                    fontWeight = FontWeight.Bold
                )
                if (originalPrice != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = originalPrice,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                    )
                }
            }
            
            if (stockStatus == StockStatus.LOW_STOCK) {
                Text(
                    text = "Low Stock",
                    style = MaterialTheme.typography.labelSmall,
                    color = CosmicPink
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            OrbitButton(text = "Add", onClick = onAdd, modifier = Modifier.height(36.dp))
        }
    }
}


