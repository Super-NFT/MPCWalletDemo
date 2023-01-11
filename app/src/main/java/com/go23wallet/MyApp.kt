package com.go23wallet

import android.app.Application
import com.coins.app.Go23WalletManage

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Go23WalletManage.getInstance().setDebug(false)
            .build(applicationContext, "OcHB6Ix8bIWiOyE35ze6Ra9e", "KX6OquHkkKQmzLSncmnmNt2q")
    }
}