package com.go23wallet.mpcwalletdemo.data

import com.coins.app.bean.chain.UserChain

data class UserWalletInfo(
    var walletId: Int = 0,
    var walletAddress: String = "",
    var userChain: UserChain = UserChain()
)
