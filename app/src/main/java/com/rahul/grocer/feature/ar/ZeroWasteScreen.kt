package com.rahul.grocer.feature.ar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rahul.grocer.ui.components.OrbitButton
import com.rahul.grocer.ui.components.OrbitGlassCard
import com.rahul.grocer.ui.theme.FreshGreen

@Composable
fun ZeroWasteScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Viewfinder Placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Text(
                text = "Camera Viewfinder",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // AR Overlay
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 100.dp) // Offset slightly up
                .background(FreshGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .border(2.dp, FreshGreen, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = "Milk (Exp: 2 days)", color = Color.White)
        }

        // Bottom Sheet
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            OrbitGlassCard {
                Column {
                    Text(
                        text = "Fridge Scan Complete",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We found 3 items expiring soon. We can make 'Creamy Pasta' with these.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OrbitButton(text = "View Recipe", onClick = { /* TODO */ })
                }
            }
        }
    }
}
