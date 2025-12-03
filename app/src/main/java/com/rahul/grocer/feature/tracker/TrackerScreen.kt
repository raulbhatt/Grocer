package com.rahul.grocer.feature.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rahul.grocer.ui.components.OrbitGlassCard
import com.rahul.grocer.ui.theme.DeepSpaceBlue
import com.rahul.grocer.ui.theme.NebulaPurple
import com.rahul.grocer.ui.theme.StarlightSilver

@Composable
fun TrackerScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Map Placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepSpaceBlue)
        ) {
            // Grid lines or map styling could go here
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Driver",
                tint = NebulaPurple,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }

        // Driver Card
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            OrbitGlassCard {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Gray, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "David is 5 mins away", style = MaterialTheme.typography.titleMedium, color = Color.White)
                            Text(text = "Toyota Prius • 4.9 ★", style = MaterialTheme.typography.bodyMedium, color = StarlightSilver)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Arriving at 7:45 PM", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                }
            }
        }
    }
}
