package com.rahul.paymentintegrationdemo

import android.content.Context
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

object PaymentsUtil {

    // API usage constants
    val PAYMENTS_ENVIRONMENT = Wallet.WalletOptions.Builder()
        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
        .build()

    // API Version
    private val SUPPORTED_NETWORKS = listOf(
        "VISA",
        "MASTERCARD",
    )

    private val SUPPORTED_METHODS = listOf(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS"
    )

    // Required by the API, but not visible to the user.
    private const val COUNTRY_CODE = "US"
    private const val CURRENCY_CODE = "USD"
    private const val MERCHANT_NAME = "Example Merchant"

    // Gateway Constants
    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "example"
    private val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
        "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
        "gatewayMerchantId" to "exampleMerchantId"
    )

    /**
     * Create a PaymentsClient instance.
     *
     * @param context specific context and app's theme.
     */
    fun createPaymentsClient(context: Context): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()

        return Wallet.getPaymentsClient(context, walletOptions)
    }

    /**
     * Base request object for Google Pay API.
     */
    private val baseRequest = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    /**
     * Gateway Tokenization Specification
     */
    private val gatewayTokenizationSpecification = JSONObject().apply {
        put("type", "PAYMENT_GATEWAY")
        put("parameters", JSONObject(PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS))
    }

    /**
     * Card Payment Method parameters
     */
    private val cardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedAuthMethods", JSONArray(SUPPORTED_METHODS))
            put("allowedCardNetworks", JSONArray(SUPPORTED_NETWORKS))
            // Optionally allow prepaid cards:
            // put("allowPrepaidCards", true)
            // Billing address required?
            // put("billingAddressRequired", true)
            // put("billingAddressParameters", JSONObject().apply { put("format", "FULL") })
        })
    }

    /**
     * Allowed Payment Methods (List of Methods)
     * For isReadyToPay, we don't strictly need tokenization specification,
     * but we attach it for loadPaymentData.
     */
    private fun baseCardPaymentMethod(): JSONObject {
        return JSONObject().apply {
            put("type", "CARD")
            put("parameters", JSONObject().apply {
                put("allowedAuthMethods", JSONArray(SUPPORTED_METHODS))
                put("allowedCardNetworks", JSONArray(SUPPORTED_NETWORKS))
            })
        }
    }
    
    // Expose just the allowed payment methods array for the UI button
    fun getAllowedPaymentMethods(): JSONArray {
        return JSONArray().put(baseCardPaymentMethod())
    }

    // For isReadyToPayRequest, we only need the base card method (no tokenization)
    fun isReadyToPayRequest(): JSONObject? {
        return try {
            val isReadyToPayRequest = JSONObject(baseRequest.toString())
            isReadyToPayRequest.put(
                "allowedPaymentMethods", getAllowedPaymentMethods()
            )
            isReadyToPayRequest
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Full Card Payment Method with Tokenization Specification
     */
    private fun cardPaymentMethodWithTokenization(): JSONObject {
         return baseCardPaymentMethod().apply {
             put("tokenizationSpecification", gatewayTokenizationSpecification)
         }
    }

    /**
     * Payment Data Request (Load Payment Data)
     */
    fun getPaymentDataRequest(priceCents: Long): JSONObject? {
        return try {
            val paymentDataRequest = JSONObject(baseRequest.toString())
            
            // Allow card payment methods
            paymentDataRequest.put(
                "allowedPaymentMethods", JSONArray().put(cardPaymentMethodWithTokenization())
            )

            // Transaction Info
            paymentDataRequest.put("transactionInfo", JSONObject().apply {
                put("totalPrice", centsToString(priceCents))
                put("totalPriceStatus", "FINAL")
                put("countryCode", COUNTRY_CODE)
                put("currencyCode", CURRENCY_CODE)
            })

            // Merchant Info
            paymentDataRequest.put("merchantInfo", JSONObject().apply {
                put("merchantName", MERCHANT_NAME)
            })

            paymentDataRequest
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Helper to format cents to string (e.g. 1234 -> "12.34")
    private fun centsToString(cents: Long): String {
        return BigDecimal(cents)
            .divide(BigDecimal(100))
            .setScale(2, RoundingMode.HALF_EVEN)
            .toString()
    }
}
