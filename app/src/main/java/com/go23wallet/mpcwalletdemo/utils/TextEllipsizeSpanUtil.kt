package com.go23wallet.mpcwalletdemo.utils

import android.content.Context
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.math.floor

object TextEllipsizeSpanUtil {
    fun setTextEndImg(context: Context, view: TextView, text: String, resId: Int, callback: () -> Unit = {}) {
        //先设置原始文本
        view.text = text
        view.highlightColor = ContextCompat.getColor(context, android.R.color.transparent);
        view.movementMethod = LinkMovementMethod.getInstance()
        view.post { //获取第一行的宽度
            val lineWidth: Float = view.layout?.getLineWidth(0) ?: return@post
            //获取第一行最后一个字符的下标
            val lineEnd: Int = view.layout.getLineEnd(0)
            //计算每个字符占的宽度
            val widthPerChar = lineWidth / (lineEnd + 1)
            //计算TextView一行能够放下多少个字符
            val numberPerLine =
                floor((view.width / widthPerChar).toDouble()).toInt()
            //在原始字符串中插入一个空格，插入的位置为numberPerLine - 1
            val stringBuilder: StringBuilder = StringBuilder(text).insert(numberPerLine - 1, " ")

            //SpannableString的构建
            val spannableString = SpannableString("$stringBuilder  ")
            ContextCompat.getDrawable(context, resId)?.apply {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                val imageSpan = ImageSpan(this, ImageSpan.ALIGN_BASELINE)
                spannableString.setSpan(
                    imageSpan,
                    spannableString.length - 1,
                    spannableString.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
//                spannableString.setSpan(
//                    object : ClickableSpan() {
//                        override fun onClick(widget: View) {
//                            callback.invoke()
//                        }
//                    }, spannableString.length - 1,
//                    spannableString.length,
//                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//                )
            }
            view.text = spannableString
        }

    }
}
