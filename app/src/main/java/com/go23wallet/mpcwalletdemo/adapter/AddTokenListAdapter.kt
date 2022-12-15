package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.token.Token
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class AddTokenListAdapter(private val mContext: Context) :
    BaseQuickAdapter<Token, BaseViewHolder>(R.layout.item_add_token_layout) {

    private var selectList: MutableList<Token> = mutableListOf()

    fun setTokenList(list: MutableList<Token>) {
        selectList = list
    }

    override fun convert(holder: BaseViewHolder, item: Token) {
        val ivTokenIcon = holder.getView<AppCompatImageView>(R.id.iv_token_icon)
        val tvTokenName = holder.getView<AppCompatTextView>(R.id.tv_token_name)
        val tvTokenNum = holder.getView<AppCompatTextView>(R.id.tv_token_num)
        val tvTokenValue = holder.getView<AppCompatTextView>(R.id.tv_token_value)
        val ivStatus = holder.getView<AppCompatImageView>(R.id.iv_status)
        GlideUtils.loadImg(mContext, item.image_url, ivTokenIcon)
        tvTokenName.text = item.name
        tvTokenNum.text = item.balance
        tvTokenValue.text = "$${item.balance_u ?: 0.00}"

        ivStatus.setImageResource(if (selectList.indexOfFirst { it.token_id == item.token_id } >= 0) R.drawable.icon_checked else R.drawable.icon_uncheck)
    }
}