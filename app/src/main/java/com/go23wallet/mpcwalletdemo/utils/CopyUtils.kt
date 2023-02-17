package com.go23wallet.mpcwalletdemo.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.EditText
import com.go23wallet.mpcwalletdemo.R

/**
 * copy util
 */
object CopyUtils {

    fun copyText(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(null, text)
        clipboard.setPrimaryClip(clipData)
        CustomToast.showShort(R.string.lite_copy_success)
    }

    fun pasteText(context: Context, etView: EditText) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = clipboard.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val text = clipData.getItemAt(0).text
            etView.setText(text)
        } else {
            CustomToast.showShort(R.string.lite_paste_error)
        }
    }
}