package com.go23wallet.mpcwalletdemo.wallet

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.TextView
import com.go23wallet.mpcwalletdemo.R

class CoinsOauthDialog constructor(context: Context, theme: Int) : Dialog(context, theme) {

    constructor(context: Context) : this(context, R.style.CommonDialog)

    init {
        window!!.decorView.setBackgroundResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)
        window!!.attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
        window!!.setGravity(Gravity.BOTTOM)
        setContentView(R.layout.dialog_oauth)
    }

    var callback: (code: String?) -> Unit = {}


    fun setView(appName: String, callback: (yes: Boolean) -> Unit = {}): CoinsOauthDialog {
        val title = context.getString(R.string.v2_oauth_title, appName)
        val sub = context.getString(R.string.v2_oauth_sub, appName)
        findViewById<TextView>(R.id.title).text = String.format(title, appName)
        findViewById<TextView>(R.id.message).text = String.format(sub, appName)
        findViewById<TextView>(R.id.yes).setOnClickListener {
            callback.invoke(true)
            dismiss()
        }
        findViewById<TextView>(R.id.no).setOnClickListener {
            callback.invoke(false)
            dismiss()
        }
        return this
    }

}