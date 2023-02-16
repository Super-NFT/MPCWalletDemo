package com.go23wallet.mpcwalletdemo.utils

import android.content.Context
import com.Go23WalletManage

object InitUtils {
    fun initMpc(app: Context, isDebug: Boolean, clientId: String, clientSecret: String) {
        Go23WalletManage.getInstance().setDebugModel(isDebug)
            .build(app, clientId, clientSecret)
    }
}