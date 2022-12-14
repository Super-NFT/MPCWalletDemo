package com.go23wallet.mpcwalletdemo.livedata

import androidx.lifecycle.MutableLiveData
import com.coins.app.bean.token.Token

object TokenListLiveData {
    val liveData = MutableLiveData<MutableList<Token>?>()
    fun setTokenList(data: MutableList<Token>?) {
        liveData.value = data
    }

    fun clearTokens() {
        liveData.value = null
    }
}