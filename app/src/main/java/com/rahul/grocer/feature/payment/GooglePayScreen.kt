

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import com.rahul.paymentintegrationdemo.PaymentsUtil

@Composable
fun GooglePayScreen(
    viewModel: GooglePayViewModel,
    onPayClicked: () -> Unit
) {
    val isReadyToPay by viewModel.isReadyToPay.collectAsState()
    val isProcessingPayment by viewModel.isProcessingPayment.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isProcessingPayment) {
            CircularProgressIndicator()
            Text("Processing payment...", modifier = Modifier.padding(top = 8.dp))
        } else {
            if (isReadyToPay) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(16.dp)),
                    factory = { context ->
                        PayButton(context).apply {
                            initialize(
                                ButtonOptions.newBuilder()
                                    .setAllowedPaymentMethods(PaymentsUtil.getAllowedPaymentMethods().toString())
                                    .build()
                            )
                            setOnClickListener { onPayClicked() }
                        }
                    }
                )
            } else {
                Text("Google Pay not available or loading...")
            }
        }
    }
}
