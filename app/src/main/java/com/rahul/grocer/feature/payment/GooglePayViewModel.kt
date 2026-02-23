
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.rahul.paymentintegrationdemo.PaymentsUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GooglePayViewModel(application: Application) : AndroidViewModel(application) {

    private val paymentsClient: PaymentsClient = PaymentsUtil.createPaymentsClient(application)

    private val _isReadyToPay = MutableStateFlow(false)
    val isReadyToPay: StateFlow<Boolean> = _isReadyToPay.asStateFlow()

    private val _isProcessingPayment = MutableStateFlow(false)
    val isProcessingPayment: StateFlow<Boolean> = _isProcessingPayment.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()

    init {
        checkIsReadyToPay()
    }

    private fun checkIsReadyToPay() {
        val requestJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(requestJson.toString())
        val task = paymentsClient.isReadyToPay(request)
        
        task.addOnCompleteListener { completedTask ->
            if (completedTask.isSuccessful) {
                _isReadyToPay.value = completedTask.result == true
            } else {
                // Handle checking failure or assume false
                _isReadyToPay.value = false
            }
        }
    }

    fun setProcessingPayment(isProcessing: Boolean) {
        _isProcessingPayment.value = isProcessing
    }

    fun getLoadPaymentDataTask(priceCents: Long): Task<PaymentData> {
        val requestJson = PaymentsUtil.getPaymentDataRequest(priceCents) 
            ?: throw IllegalStateException("Invalid Payment Data Request")
        val request = PaymentDataRequest.fromJson(requestJson.toString())
        return paymentsClient.loadPaymentData(request)
    }

    fun showSuccessDialog(show: Boolean) {
        _showSuccessDialog.value = show
    }
}