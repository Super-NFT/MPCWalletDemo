package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23.bean.token.Token
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class AddTokenListAdapter(private val mContext: Context) :
    BaseQuickAdapter<Token, BaseViewHolder>(R.layout.item_add_token_layout) {

    override fun convert(holder: BaseViewHolder, item: Token) {
        val ivTokenIcon = holder.getView<AppCompatImageView>(R.id.iv_token_icon)
        val tvTokenName = holder.getView<AppCompatTextView>(R.id.tv_token_name)
        val tvTokenNum = holder.getView<AppCompatTextView>(R.id.tv_token_num)
        val tvTokenValue = holder.getView<AppCompatTextView>(R.id.tv_token_value)
        val ivStatus = holder.getView<AppCompatImageView>(R.id.iv_status)
        GlideUtils.loadImg(mContext, item.image_url, ivTokenIcon)
        tvTokenName.text = item.symbol
//        tvTokenNum.visibility = if (item.balance.isNullOrEmpty()) View.GONE else View.VISIBLE
//        tvTokenNum.text = item.balance
//        tvTokenValue.visibility = if (item.balance_u.isNullOrEmpty()) View.GONE else View.VISIBLE
//        tvTokenValue.text = "$${item.balance_u ?: 0.00}"
        ivStatus.visibility = if (item.isIs_platform) View.GONE else View.VISIBLE
        ivStatus.setImageResource(if (item.isIs_selected) R.drawable.icon_checked else R.drawable.icon_uncheck)
    }
}