package com.rahul.grocer.feature.support

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rahul.grocer.ui.components.OrbitGlassCard
import com.rahul.grocer.ui.theme.CosmicPink
import com.rahul.grocer.ui.theme.NebulaPurple
import com.rahul.grocer.ui.theme.StarlightSilver

@Composable
fun SupportScreen(
    onNavigateToOrbitAI: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Help & Support",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Quick Actions
            item {
                Text(
                    text = "Quick Connect",
                    style = MaterialTheme.typography.titleMedium,
                    color = StarlightSilver
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SupportActionButton(
                        icon = Icons.Default.Email,
                        label = "Chat",
                        modifier = Modifier.weight(1f),
                        onClick = { /* TODO: Open Chat */ }
                    )
                    SupportActionButton(
                        icon = Icons.Default.Call,
                        label = "Call",
                        modifier = Modifier.weight(1f),
                        onClick = { /* TODO: Call Dummy Number */ }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                OrbitGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToOrbitAI)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = NebulaPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ask Orbit AI",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }

            // Quick Issues
            item {
                Text(
                    text = "Common Issues",
                    style = MaterialTheme.typography.titleMedium,
                    color = StarlightSilver
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickIssueChip(label = "Refund", modifier = Modifier.weight(1f))
                    QuickIssueChip(label = "Missing Item", modifier = Modifier.weight(1f))
                    QuickIssueChip(label = "Delay", modifier = Modifier.weight(1f))
                }
            }

            // FAQ
            item {
                Text(
                    text = "Frequently Asked Questions",
                    style = MaterialTheme.typography.titleMedium,
                    color = StarlightSilver
                )
                Spacer(modifier = Modifier.height(16.dp))
                FAQItem(question = "Where is my order?", answer = "You can track your order in real-time from the 'Track Order' section in your profile.")
                FAQItem(question = "How do I request a refund?", answer = "Go to 'My Orders', select the order, and tap on 'Request Refund'. Refunds are processed within 24 hours.")
                FAQItem(question = "Can I change my delivery address?", answer = "You can change the address before the order is packed. Go to 'My Orders' to modify.")
                FAQItem(question = "What payment methods are accepted?", answer = "We accept all major credit cards, UPI, and Orbit Wallet.")
            }
            
            item {
                 Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SupportActionButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OrbitGlassCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = MaterialTheme.typography.titleSmall, color = Color.White)
        }
    }
}

@Composable
fun QuickIssueChip(
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .clickable { /* TODO: Show dialog */ }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    
    OrbitGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = StarlightSilver
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = StarlightSilver
                )
            }
        }
    }
}
