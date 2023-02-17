package com.go23wallet.mpcwalletdemo.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserInfo(
    val uniqueId: String,
    val nickname: String,
    val avatar: String,
    val email: String,
    val dialCode: String,
    val phone: String
) : Parcelable