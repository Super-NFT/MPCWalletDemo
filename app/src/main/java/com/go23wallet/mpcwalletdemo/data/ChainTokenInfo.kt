package com.go23wallet.mpcwalletdemo.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ChainTokenInfo(
    val chain_id: Int,
    val user_wallet_address: String,
    val name: String,
    val symbol: String,
    val imgUrl: String,
    val contract_address: String,
) : Parcelable