package com.go23wallet.mpcwalletdemo.livedata

import androidx.lifecycle.MutableLiveData

object UpdateDataLiveData {
    /**
     * 1： token
     * 2： nft
     */
    val liveData = MutableLiveData<Int>()
    fun setUpdateType(type: Int) {
        liveData.postValue(type)
    }

    fun clearType() {
        liveData.value = 0
    }
}