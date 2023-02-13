package com.go23wallet.mpcwalletdemo.ext

import com.blankj.utilcode.util.StringUtils

fun String.parseAddress(): String {
    if (StringUtils.isEmpty(this)) {
        return this
    }
    val beforeStr = this.substring(0, 4)
    val afterStr = this.substring(length - 4, length)
    val sb = StringBuffer()
    sb.append(beforeStr).append("...").append(afterStr)
    return sb.toString()
}

fun String.parseContractAddress(): String {
    if (StringUtils.isEmpty(this)) {
        return this
    }
    val beforeStr = this.substring(0, 8)
    val afterStr = this.substring(length - 8, length)
    val sb = StringBuffer()
    sb.append(beforeStr).append("...").append(afterStr)
    return sb.toString()
}

fun String.checkNullOrZero(): String {
    return try {
        if (this.isNullOrEmpty() || this.toDouble() == 0.0) {
            "0.00"
        } else {
            this
        }
    } catch (e: Exception) {
        "0.00"
    }
}

fun String.hideOrShowValue(isShow: Boolean, suffix: String? = null): String {
    if (StringUtils.isEmpty(this)) {
        return this
    }
    return if (isShow) {
        if (suffix.isNullOrEmpty()) {
            this
        } else {
            "$this $suffix"
        }
    } else {
        if (suffix.isNullOrEmpty()) {
            "****"
        } else {
            "**** $suffix"
        }

    }
}