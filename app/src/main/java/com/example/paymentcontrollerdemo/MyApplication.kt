package com.example.paymentcontrollerdemo

import android.app.Application
import vn.teko.terra.core.android.terra.TerraApp

class MyApplication: Application() {

    private lateinit var terraApp: TerraApp

    override fun onCreate() {
        super.onCreate()

        terraApp = TerraApp.initializeApp(
            application = this,
            appName = "payment_controller"
        )
    }
}