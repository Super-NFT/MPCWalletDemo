package com.go23wallet.mpcwalletdemo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChainTokenInfo(
    val blockChainId: Int,
    val addr: String,
    val name: String,
    val symbol: String,
    val imgUrl: String,
) : Parcelable