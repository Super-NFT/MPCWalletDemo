package com.go23wallet.mpcwalletdemo.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserInfo(
    val uniqueId: String,
    val email: String,
    val phoneInfo: PhoneInfo,
    val nickname: String? = "",
    val avatar: String? = "",
) : Parcelable

@Keep
@Parcelize
data class PhoneInfo(
    val dialCode: String,
    val phone: String
) : Parcelable
