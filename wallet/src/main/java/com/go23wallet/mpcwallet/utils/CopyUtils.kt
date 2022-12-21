package com.go23wallet.mpcwallet.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.go23wallet.mpcwallet.R

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

    fun pasteText(context: Context, etView: EditText) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = clipboard.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val text = clipData.getItemAt(0).text
            etView.setText(text)
        } else {
            Toast.makeText(context, context.getText(R.string.paste_error), Toast.LENGTH_SHORT)
                .show()
        }
    }
}