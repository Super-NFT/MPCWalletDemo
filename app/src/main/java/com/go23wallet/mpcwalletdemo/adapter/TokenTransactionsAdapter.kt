package com.go23wallet.mpcwalletdemo.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.transaction.Transaction
import com.go23wallet.mpcwalletdemo.R

class TokenTransactionsAdapter: BaseQuickAdapter<Transaction, BaseViewHolder>(R.layout.layout_token_type_item) {

    override fun convert(holder: BaseViewHolder, item: Transaction) {
        val ivIcon = holder.getView<AppCompatImageView>(R.id.iv_icon)
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val tvTime = holder.getView<AppCompatTextView>(R.id.tv_time)
        val tvChargeNum = holder.getView<AppCompatTextView>(R.id.tv_charge_num)
        val tvValue = holder.getView<AppCompatTextView>(R.id.tv_value)
        ivIcon.setImageResource(R.drawable.icon_type_out)
        tvTitle.text = item.contract
        tvTime.text = item.contract
        tvChargeNum.text = item.contract
        tvValue.text = item.contract
    }
}