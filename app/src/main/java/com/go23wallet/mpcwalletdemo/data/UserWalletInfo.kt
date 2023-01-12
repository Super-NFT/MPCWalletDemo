package com.go23wallet.mpcwalletdemo.data

import androidx.annotation.Keep
import com.coins.app.bean.chain.UserChain
import com.coins.app.bean.walletinfo.WalletInfo

@Keep
data class UserWalletInfo(
    var userChain: UserChain = UserChain(),
    var walletInfo: WalletInfo = WalletInfo()
)
