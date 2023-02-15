package com.go23wallet.mpcwalletdemo.data

data class CountryCodeResponse(
    val code: String,
    val message: String,
    val data: MutableList<CountryCodeInfo>
)

data class CountryCodeInfo(
    val code: String,
    val dial_code: String,
    val flag_emoji: String,
    val name: String
)