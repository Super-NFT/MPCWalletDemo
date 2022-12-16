package com.go23wallet.mpcwalletdemo.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.transaction.Transaction
import com.go23wallet.mpcwalletdemo.R

class TokenTransactionsAdapter :
    BaseQuickAdapter<Transaction, BaseViewHolder>(R.layout.layout_token_type_item) {

    override fun convert(holder: BaseViewHolder, item: Transaction) {
        val ivIcon = holder.getView<AppCompatImageView>(R.id.iv_icon)
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val tvTime = holder.getView<AppCompatTextView>(R.id.tv_time)
        val tvBalance = holder.getView<AppCompatTextView>(R.id.tv_charge_balance)
        val tvValue = holder.getView<AppCompatTextView>(R.id.tv_value)
        tvBalance.setTextColor(context.getColor(R.color.color_262626))
        when (item.transaction_type.toString()) {
            "in" -> {
                ivIcon.setImageResource(R.drawable.icon_type_in)
                tvTitle.text = "Receive ${item.from_addr}"
                tvBalance.setTextColor(context.getColor(R.color.color_35C1D8))
            }
            "out" -> {
                ivIcon.setImageResource(R.drawable.icon_type_out)
                tvTitle.text = "Send ${item.to_addr}"
            }
            "fail" -> {
                ivIcon.setImageResource(R.drawable.icon_type_failed)
                tvTitle.text = "Receive ${item.from_addr}"
            }
            else -> {
                ivIcon.setImageResource(R.drawable.icon_type_failed)
                tvTitle.text = "Receive ${item.from_addr}"
            }
        }
        ivIcon.setImageResource(R.drawable.icon_type_out)
        tvTime.text = item.contract
        tvBalance.text = item.contract
        tvValue.text = item.contract
    }
}