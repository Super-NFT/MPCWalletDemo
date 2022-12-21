package com.go23wallet.mpcwallet.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.token.Token
import com.go23wallet.mpcwallet.R
import com.go23wallet.mpcwallet.utils.GlideUtils

class SelectTokenAdapter :
    BaseQuickAdapter<Token, BaseViewHolder>(R.layout.item_select_token_layout) {

    override fun convert(holder: BaseViewHolder, item: Token) {
        val ivIcon = holder.getView<AppCompatImageView>(R.id.iv_token_icon)
        val ivCorner = holder.getView<AppCompatImageView>(R.id.iv_corner)
        val tvName = holder.getView<AppCompatTextView>(R.id.tv_token_name)
        val tvTokenNum = holder.getView<AppCompatTextView>(R.id.tv_token_num)
        val tvTokenValue = holder.getView<AppCompatTextView>(R.id.tv_token_value)
        GlideUtils.loadImg(context, item.image_url, ivIcon)
        GlideUtils.loadImg(context, item.chain_image_url, ivCorner)
        tvName.text = item.symbol
        tvTokenNum.text = item.balance
        tvTokenValue.text = "$${item.balance_u}"
    }

}