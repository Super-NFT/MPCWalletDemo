package com.go23wallet.mpcwalletdemo.livedata

import androidx.lifecycle.MutableLiveData

object TokenListLiveData {
    val liveData = MutableLiveData<MutableList<String>?>()
    fun setTokenList(data: MutableList<String>) {
        liveData.value = data
    }

    fun clearTokens() {
        liveData.value = null
    }
}