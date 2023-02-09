package com.go23wallet.mpcwalletdemo.data

import androidx.annotation.Keep
import androidx.annotation.StringRes

@Keep
data class DappItem(
    val title: String,
    val content: String,
    val url: String,
    val icon: Int,
    val chainIcon: Int
)