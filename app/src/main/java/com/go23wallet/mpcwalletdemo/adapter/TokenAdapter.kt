package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23.bean.token.Token
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.ext.checkNullOrZero
import com.go23wallet.mpcwalletdemo.ext.hideOrShowValue
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class TokenAdapter(private val mContext: Context) :
    BaseQuickAdapter<Token, BaseViewHolder>(R.layout.layout_token_item) {

    private var isShowBalance = true

    fun setIsShowBalance(isShowBalance: Boolean) {
        this.isShowBalance = isShowBalance
        notifyDataSetChanged()
    }

    override fun convert(holder: BaseViewHolder, item: Token) {
        val ivIcon = holder.getView<AppCompatImageView>(R.id.iv_icon)
        val ivCorner = holder.getView<AppCompatImageView>(R.id.iv_corner)
        val tvName = holder.getView<AppCompatTextView>(R.id.tv_name)
        val tvPrice = holder.getView<AppCompatTextView>(R.id.tv_price)
        val tvBalance = holder.getView<AppCompatTextView>(R.id.tv_balance)
        val tvBalanceU = holder.getView<AppCompatTextView>(R.id.tv_balance_u)
        GlideUtils.loadImg(mContext, item.image_url, ivIcon)
        GlideUtils.loadImg(mContext, item.chain_image_url, ivCorner)
        tvName.text = item.symbol
        "$${item.token_value}".also { tvPrice.text = it }
        tvBalance.text = item.balance.checkNullOrZero().hideOrShowValue(isShowBalance)
        tvBalanceU.text = item.balance_u.checkNullOrZero(true).hideOrShowValue(isShowBalance)
    }
}