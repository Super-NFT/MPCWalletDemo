package com.go23wallet.mpcwalletdemo.data

import androidx.annotation.Keep
import com.go23.bean.chain.UserChain
import com.go23.bean.walletinfo.WalletInfo

@Keep
data class UserWalletInfo(
    var userChain: UserChain = UserChain(),
    var walletInfo: WalletInfo = WalletInfo()
)
