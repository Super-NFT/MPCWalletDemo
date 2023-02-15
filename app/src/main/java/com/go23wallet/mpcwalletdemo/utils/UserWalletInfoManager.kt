package com.go23wallet.mpcwalletdemo.utils

import com.go23.bean.chain.UserChain
import com.go23.bean.walletinfo.WalletInfo
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