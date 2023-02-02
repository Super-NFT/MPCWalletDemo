package com.example.start

import android.app.Application
import com.go23wallet.mpcwalletdemo.utils.InitUtils

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        InitUtils.initMpc(this, false, "OcHB6Ix8bIWiOyE35ze6Ra9e", "KX6OquHkkKQmzLSncmnmNt2q")
    }
}