package com.go23wallet

import android.app.Application
import com.coins.app.Go23WalletManage

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
//        Go23WalletManage.getInstance().setDebug(false)
//            .build(applicationContext, "OcHB6Ix8bIWiOyE35ze6Ra9e", "KX6OquHkkKQmzLSncmnmNt2q")
        Go23WalletManage.getInstance().setDebug(true)
            .build(applicationContext, "j9ASxn5REHG8akytevRYZwCp", "QHXFT28Nu1u4R7IiGBlFCVXF")
    }
}