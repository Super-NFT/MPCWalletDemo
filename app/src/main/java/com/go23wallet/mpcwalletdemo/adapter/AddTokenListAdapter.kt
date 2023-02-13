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
import com.go23wallet.mpcwalletdemo.ext.parseAddress
import com.go23wallet.mpcwalletdemo.ext.parseContractAddress
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class AddTokenListAdapter(private val mContext: Context) :
    BaseQuickAdapter<Token, BaseViewHolder>(R.layout.item_add_token_layout) {

    override fun convert(holder: BaseViewHolder, item: Token) {
        val ivTokenIcon = holder.getView<AppCompatImageView>(R.id.iv_token_icon)
        val tvTokenName = holder.getView<AppCompatTextView>(R.id.tv_token_name)
        val tvContractAddress = holder.getView<AppCompatTextView>(R.id.tv_contract_address)
        val tvBalance = holder.getView<AppCompatTextView>(R.id.tv_balance)
        val tvBalanceU = holder.getView<AppCompatTextView>(R.id.tv_balance_u)
        val ivStatus = holder.getView<AppCompatImageView>(R.id.iv_status)
        GlideUtils.loadImg(mContext, item.image_url, ivTokenIcon)
        tvTokenName.text = item.symbol
        tvContractAddress.text =
            if (item.contract_address.isNullOrEmpty()) item.chain_name else item.contract_address.parseContractAddress()
        tvBalance.text = item.balance.checkNullOrZero()
        tvBalanceU.text = item.balance_u.checkNullOrZero()
        ivStatus.setImageResource(if (item.isIs_selected) R.drawable.icon_checked else R.drawable.icon_uncheck)
    }
}