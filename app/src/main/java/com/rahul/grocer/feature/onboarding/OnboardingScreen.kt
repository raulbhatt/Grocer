package com.rahul.grocer.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rahul.grocer.ui.components.OrbitButton
import com.rahul.grocer.ui.components.OrbitGlassCard
import com.rahul.grocer.ui.components.OrbitTextField
import com.rahul.grocer.ui.theme.NebulaPurple
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    var step by remember { mutableStateOf(0) }
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(targetState = step, label = "OnboardingStep") { currentStep ->
            when (currentStep) {
                0 -> AddressStep(
                    address = address,
                    onAddressChange = { address = it },
                    onNext = { step = 1 }
                )
                1 -> PaymentStep(
                    paymentMethod = paymentMethod,
                    onPaymentMethodChange = { paymentMethod = it },
                    onNext = { step = 2 }
                )
                2 -> SuccessStep(onComplete = onOnboardingComplete)
            }
        }
    }
}

@Composable
fun AddressStep(
    address: String,
    onAddressChange: (String) -> Unit,
    onNext: () -> Unit
) {
    OrbitGlassCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = NebulaPurple,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Where are we sending this?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We'll find the nearest dark store.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            OrbitTextField(
                value = address,
                onValueChange = onAddressChange,
                label = "Enter Address",
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            OrbitButton(
                text = "Confirm Location",
                onClick = onNext,
                enabled = address.isNotEmpty()
            )
        }
    }
}

@Composable
fun PaymentStep(
    paymentMethod: String,
    onPaymentMethodChange: (String) -> Unit,
    onNext: () -> Unit
) {
    OrbitGlassCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Payment",
                tint = NebulaPurple,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How would you like to pay?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            OrbitTextField(
                value = paymentMethod,
                onValueChange = onPaymentMethodChange,
                label = "Card Number",
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            OrbitButton(
                text = "Link Payment",
                onClick = onNext,
                enabled = paymentMethod.isNotEmpty()
            )
        }
    }
}

@Composable
fun SuccessStep(onComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) // Simulate loading/setup
        onComplete()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Success",
            tint = NebulaPurple,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "All Systems Go",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Launching Orbit...",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
