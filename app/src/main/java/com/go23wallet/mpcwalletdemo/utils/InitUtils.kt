package com.go23wallet.mpcwalletdemo.utils

import android.app.Application
import com.Go23WalletManage

object InitUtils {
    fun initMpc(app: Application, isDebug: Boolean, clientId: String, clientSecret: String) {
        Go23WalletManage.getInstance().setDebugModel(isDebug)
            .build(app, clientId, clientSecret)
    }
}