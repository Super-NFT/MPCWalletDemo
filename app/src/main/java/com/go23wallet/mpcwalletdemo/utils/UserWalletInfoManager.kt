package com.go23wallet.mpcwalletdemo.utils

import com.coins.app.bean.chain.UserChain
import com.go23wallet.mpcwalletdemo.data.UserWalletInfo

object UserWalletInfoManager {
    private val userWalletInfo = UserWalletInfo()

    fun setWalletId(id: Int) {
        userWalletInfo.walletId = id
    }

    fun setAccountId(id: Int) {
        userWalletInfo.accountId = id
    }

    fun serUserChain(userChain: UserChain) {
        userWalletInfo.userChain = userChain
    }

    fun setWalletAddr(addr: String) {
        userWalletInfo.walletAddress = addr
    }

    fun getUserWalletInfo(): UserWalletInfo {
        return userWalletInfo
    }
}