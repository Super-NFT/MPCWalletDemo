package com.go23wallet.mpcwalletdemo.utils

import android.view.Gravity
import android.widget.TextView
import androidx.annotation.StringRes
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.ViewUtils
import com.go23wallet.mpcwalletdemo.R

object CustomToast {

    fun showShort(text: CharSequence) {
        show(text, false)
    }

    fun showShort(@StringRes resId: Int) {
        show(StringUtils.getString(resId), false)
    }

    fun showShort(@StringRes resId: Int, vararg args: Any) {
        show(StringUtils.getString(resId, args), false)
    }

    fun showShort(format: String, vararg args: Any) {
        show(StringUtils.format(format, args), false)
    }

    fun showLong(text: CharSequence) {
        show(text, true)
    }

    fun showLong(@StringRes resId: Int) {
        show(StringUtils.getString(resId), true)
    }

    fun showLong(@StringRes resId: Int, vararg args: Any) {
        show(StringUtils.getString(resId, args), true)
    }

    fun showLong(format: String, vararg args: Any) {
        show(StringUtils.format(format, args), true)
    }

    private fun show(text: CharSequence, isLong: Boolean) {
        val textView = ViewUtils.layoutId2View(R.layout.layout_toast_custom) as TextView
        textView.text = text
        ToastUtils.make().setDurationIsLong(isLong).setGravity(Gravity.TOP, 0, 100).show(textView)
    }

    fun cancel() {
        ToastUtils.cancel()
    }
}
