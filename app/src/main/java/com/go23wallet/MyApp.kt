package com.go23wallet

import android.app.Application
import com.Go23WalletManage
import com.go23wallet.mpcwalletdemo.BuildConfig

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Go23WalletManage.getInstance().setDebugModel(BuildConfig.DEBUG).setUseStyleFont(true)
            .build(applicationContext, BuildConfig.APP_KEY, BuildConfig.APP_SECRET)
    }
}