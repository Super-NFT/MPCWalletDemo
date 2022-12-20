package com.go23wallet.mpcwalletdemo.data

import com.coins.app.bean.chain.UserChain
import com.coins.app.bean.walletinfo.WalletInfo

data class UserWalletInfo(
    var userChain: UserChain = UserChain(),
    var walletInfo: WalletInfo = WalletInfo()
)
