package com.example.start

import android.app.Application
import com.go23wallet.mpcwalletdemo.utils.InitUtils

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        InitUtils.initMpc(this, BuildConfig.DEBUG, BuildConfig.APP_KEY, BuildConfig.APP_SECRET)
    }
}