package com.go23wallet.mpcwalletdemo.utils

import com.coins.app.bean.chain.UserChain
import com.coins.app.bean.walletinfo.WalletInfo
import com.go23wallet.mpcwalletdemo.data.UserWalletInfo

object UserWalletInfoManager {
    private val userWalletInfo = UserWalletInfo()

    fun setWalletInfo(info: WalletInfo) {
        userWalletInfo.walletInfo = info
    }

    fun serUserChain(userChain: UserChain) {
        userWalletInfo.userChain = userChain
    }

    fun getUserWalletInfo(): UserWalletInfo {
        return userWalletInfo
    }
}