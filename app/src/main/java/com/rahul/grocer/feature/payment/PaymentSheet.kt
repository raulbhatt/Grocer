package com.rahul.grocer.feature.payment

import GooglePayScreen
import GooglePayViewModel
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.wallet.AutoResolveHelper
import com.rahul.grocer.ui.components.OrbitButton
import com.rahul.grocer.ui.theme.DeepSpaceBlue
import com.rahul.grocer.ui.theme.NebulaPurple
import com.rahul.grocer.ui.theme.StarlightSilver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

enum class PaymentMethod(val title: String, val icon: ImageVector) {
    Card("Credit/Debit Card", Icons.Default.Lock), // Replaced CreditCard with Lock
    UPI("UPI", Icons.Default.Phone), // Replaced PhoneAndroid with Phone
    GOOGLEPAY("Google Pay", Icons.Default.CheckCircle), // Google pay icon
    NetBanking("Net Banking", Icons.Default.Home), // Replaced AccountBalance with Home
    Wallet("Wallets", Icons.Default.Person), // Replaced AccountBalanceWallet with Person
    COD("Cash on Delivery", Icons.Default.Check) // Replaced Money with Check
}

@Composable
fun PaymentSheet(
    isVisible: Boolean,
    totalAmount: Double,
    onDismiss: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf(PaymentMethod.Card) }
    var isProcessing by remember { mutableStateOf(false) }
    var isPaymentSuccessful by remember { mutableStateOf(false) }
    var processingStep by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val viewModel: GooglePayViewModel = viewModel()

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable(enabled = !isProcessing) { onDismiss() }, // Close on outside click
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .clickable(enabled = false) {}, // Prevent click through
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceBlue)
            ) {
                if (isPaymentSuccessful) {
                    OrderConfirmedScreen()
                } else if (isProcessing) {
                    ProcessingScreen(step = processingStep)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        // Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Green, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Secure Checkout",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))

                        // Amount
                        Text(
                            text = "Total to Pay",
                            style = MaterialTheme.typography.bodyMedium,
                            color = StarlightSilver,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "$${String.format("%.2f", totalAmount)}",
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Payment Methods List (Horizontal)
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text("Select Payment Method", color = StarlightSilver, style = MaterialTheme.typography.titleSmall)
                            }
                            
                            items(PaymentMethod.values()) { method ->
                                PaymentMethodItem(
                                    method = method,
                                    isSelected = selectedMethod == method,
                                    onClick = { selectedMethod = method }
                                )
                            }
                            
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                // Selected Method Details
                                AnimatedContent(targetState = selectedMethod, label = "PaymentDetails") { method ->
                                    if (method != PaymentMethod.GOOGLEPAY) {
                                        PaymentDetailsSection(method = method,viewModel=viewModel)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (selectedMethod != PaymentMethod.GOOGLEPAY) {
                            OrbitButton(
                                text = "Pay $${String.format("%.2f", totalAmount)}",
                                onClick = {
                                    isProcessing = true
                                    scope.launch {
                                        // Simulation of Secure Gateway
                                        processingStep = "Connecting to Secure Gateway..."
                                        delay(1500)
                                        processingStep = "Verifying Payment Details..."
                                        delay(1500)
                                        processingStep = "Running Fraud Checks..."
                                        delay(2000)
                                        processingStep = "Payment Approved!"
                                        delay(1000)
                                        isPaymentSuccessful = true
                                        delay(3000) // Show confirmation for 3 seconds
                                        onPaymentSuccess()
                                        isProcessing = false
                                        isPaymentSuccessful = false
                                    }
                                }
                            )
                        } else {
                            GooglePayScreen(
                                viewModel = viewModel,
                                onPayClicked = {
                                    isProcessing = true
                                    scope.launch {
                                        // Simulation of Secure Gateway
                                        processingStep = "Connecting to Secure Gateway..."
                                        delay(1500)
                                        processingStep = "Verifying Payment Details..."
                                        delay(1500)
                                        processingStep = "Running Fraud Checks..."
                                        delay(2000)
                                        processingStep = "Payment Approved!"
                                        delay(1000)
                                        isPaymentSuccessful = true
                                        delay(3000) // Show confirmation for 3 seconds
                                        onPaymentSuccess()
                                        isProcessing = false
                                        isPaymentSuccessful = false
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun requestPayment() {
    // Price would usually be determined by selected items
    val priceCents = 1000L // $10.00
    //val task = viewModel().getLoadPaymentDataTask(priceCents)
    //AutoResolveHelper.resolveTask(task, this, LOAD_PAYMENT_DATA_REQUEST_CODE)
}

@Composable
fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NebulaPurple.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = if (isSelected) NebulaPurple else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = method.icon,
            contentDescription = null,
            tint = if (isSelected) NebulaPurple else StarlightSilver
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = method.title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Color.White else StarlightSilver,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NebulaPurple)
        }
    }
}

@Composable
fun PaymentDetailsSection(method: PaymentMethod,viewModel :GooglePayViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        when (method) {
            PaymentMethod.Card -> {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Card Number") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NebulaPurple,
                        unfocusedBorderColor = StarlightSilver
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("MM/YY") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NebulaPurple,
                            unfocusedBorderColor = StarlightSilver
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NebulaPurple,
                            unfocusedBorderColor = StarlightSilver
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
            PaymentMethod.UPI -> {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("UPI ID (e.g. user@upi)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NebulaPurple,
                        unfocusedBorderColor = StarlightSilver
                    )
                )
            }
            PaymentMethod.NetBanking -> {
                Text("Redirecting to your bank's secure login page...", color = StarlightSilver)
            }
            PaymentMethod.Wallet -> {
                Text("Redirecting to wallet authorization...", color = StarlightSilver)
            }
            PaymentMethod.COD -> {
                Text("Pay cash upon delivery. Please keep exact change ready.", color = StarlightSilver)
            }
            else -> {}
        }
    }
}

@Composable
fun ProcessingScreen(step: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = NebulaPurple,
            modifier = Modifier.size(64.dp),
            strokeWidth = 6.dp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = step,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Green) // Replaced Security with Lock
            Spacer(modifier = Modifier.width(8.dp))
            Text("Bank-grade Security", color = Color.Green)
        }
    }
}

@Composable
fun OrderConfirmedScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color.Green,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Order Confirmed",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your order will be at your doorstep soon",
            style = MaterialTheme.typography.bodyLarge,
            color = StarlightSilver,
            textAlign = TextAlign.Center
        )
    }
}
