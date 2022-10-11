package com.example.paymentcontrollerdemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vn.teko.android.payment.controller.model.CancellationConfig
import vn.teko.android.payment.controller.observer.CancellationObserver
import vn.teko.android.payment.controller.observer.terra.TerraPaymentControllerObserver
import vn.teko.android.payment.manager.TerraPayment
import vn.teko.android.payment.v2.IPaymentGateway
import vn.teko.terra.core.android.terra.TerraApp

class MainActivity : AppCompatActivity() {

    private lateinit var terraApp: TerraApp
    private lateinit var paymentGateway: IPaymentGateway
    private lateinit var observer: CancellationObserver

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        terraApp = TerraApp.getInstance("payment_controller")
        paymentGateway = TerraPayment.getInstance(terraApp)
        observer = TerraPaymentControllerObserver.getInstance(this, terraApp)
        observer.initializeObserver(CancellationConfig(paymentGateway.config.getMerchantCode(), "PE24194350087"))

        findViewById<TextView>(R.id.textMerchantId).text = paymentGateway.config.getMerchantCode()
        findViewById<TextView>(R.id.textTerminal).text = "PE24194350087"

        findViewById<Button>(R.id.buttonObserver).setOnClickListener {

            if (!terraApp.isInitialized()) {
                Toast.makeText(this, "Please init Terrra App", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!this::paymentGateway.isInitialized) {
                Toast.makeText(this, "Please init Payment Gateway", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val requestId = findViewById<TextView>(R.id.textRequestId).text.toString()
            findViewById<TextView>(R.id.textTransactionCodeObserver).text = ""
            findViewById<TextView>(R.id.textRequestIdObserver).text = ""
            findViewById<TextView>(R.id.textOrderCodeObserver).text = ""
            findViewById<TextView>(R.id.textMethodCodeObserver).text = ""
            findViewById<TextView>(R.id.textClientTransactionCodeObserver).text = ""
            findViewById<TextView>(R.id.textAmountObserver).text = ""
            findViewById<TextView>(R.id.textCreatedAtObserver).text = ""

            observer.observer(requestId).onEach {
                findViewById<TextView>(R.id.textTransactionCodeObserver).text = it.transactionCode
                findViewById<TextView>(R.id.textRequestIdObserver).text = it.requestId
                findViewById<TextView>(R.id.textOrderCodeObserver).text = it.orderCode
                findViewById<TextView>(R.id.textMethodCodeObserver).text = it.methodCode
                findViewById<TextView>(R.id.textClientTransactionCodeObserver).text = it.clientTransactionCode
                findViewById<TextView>(R.id.textAmountObserver).text = it.amount.toString()
                findViewById<TextView>(R.id.textCreatedAtObserver).text = it.createAt.toString()
            }.launchIn(lifecycleScope)
        }
    }
}