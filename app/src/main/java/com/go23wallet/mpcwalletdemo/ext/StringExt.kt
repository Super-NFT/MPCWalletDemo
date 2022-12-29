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