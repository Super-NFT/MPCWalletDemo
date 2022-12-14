package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.token.Token
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class TokenAdapter(private val mContext: Context): BaseQuickAdapter<Token, BaseViewHolder>(R.layout.layout_token_item) {

    override fun convert(holder: BaseViewHolder, item: Token) {
        val ivIcon = holder.getView<AppCompatImageView>(R.id.iv_icon)
        val ivCorner = holder.getView<AppCompatImageView>(R.id.iv_corner)
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val tvContent = holder.getView<AppCompatTextView>(R.id.tv_content)
        GlideUtils.loadImg(mContext, item.url, ivIcon)
        GlideUtils.loadImg(mContext, item.url, ivCorner)
        tvTitle.text = "${item.balance} ${item.symbol}"
        tvContent.text = item.name
    }
}