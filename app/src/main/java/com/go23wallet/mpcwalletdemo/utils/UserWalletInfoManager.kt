package com.go23wallet.mpcwalletdemo.utils

import com.go23wallet.mpcwalletdemo.data.UserWalletInfo

object UserWalletInfoManager {
    private val userWalletInfo = UserWalletInfo()

    fun setUserWalletId(id: Int) {
        userWalletInfo.userWalletId = id
    }

    fun setUserChainId(id: Int) {
        userWalletInfo.userChainId = id
    }

    fun getUserWalletInfo(): UserWalletInfo {
        return userWalletInfo
    }
}