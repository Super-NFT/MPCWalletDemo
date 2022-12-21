package com.go23wallet.mpcwallet.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChainTokenInfo(
    val chain_id: Int,
    val user_wallet_address: String,
    val name: String,
    val symbol: String,
    val imgUrl: String,
    val contract_address: String,
) : Parcelable