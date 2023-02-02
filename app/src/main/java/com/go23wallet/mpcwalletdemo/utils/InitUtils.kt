package com.go23wallet.mpcwalletdemo.utils

import android.app.Application
import com.coins.app.Go23WalletManage

object InitUtils {
    fun initMpc(app: Application, isDebug: Boolean, clientId: String, clientSecret: String) {
        Go23WalletManage.getInstance().setDebug(isDebug)
            .build(app, clientId, clientSecret)
    }
}