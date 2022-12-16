package com.go23wallet.mpcwalletdemo.utils

import com.go23wallet.mpcwalletdemo.data.UserWalletInfo

object UserWalletInfoManager {
    private val userWalletInfo = UserWalletInfo()

    fun setUserWalletId(id: Int) {
        userWalletInfo.userWalletId = id
    }

    fun setUserBlockChainId(id: Int) {
        userWalletInfo.userBlockChainId = id
    }

    fun setWalletAddr(addr: String) {
        userWalletInfo.walletAddress = addr
    }

    fun setUserChainId(id: Int) {
        userWalletInfo.userChainId = id
    }

    fun getUserWalletInfo(): UserWalletInfo {
        return userWalletInfo
    }
}