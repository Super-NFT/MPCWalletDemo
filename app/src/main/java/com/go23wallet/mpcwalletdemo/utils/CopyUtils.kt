package com.go23wallet.mpcwalletdemo.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.go23wallet.mpcwalletdemo.R

/**
 * copy util
 */
object CopyUtils {

    fun copyText(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, text)
        clipboard.setPrimaryClip(clipData)
        Toast.makeText(context, context.getText(R.string.copy_success), Toast.LENGTH_SHORT).show()
    }
}