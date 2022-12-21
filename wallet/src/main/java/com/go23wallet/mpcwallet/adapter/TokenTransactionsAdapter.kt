package com.go23wallet.mpcwallet.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.transaction.Transaction
import com.go23wallet.mpcwallet.R

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
                tvBalance.text = "+${item.value}${item.symbol}"
            }
            "out" -> {
                ivIcon.setImageResource(R.drawable.icon_type_out)
                tvTitle.text = "Send ${item.to_addr}"
                tvBalance.text = "-${item.value}${item.symbol}"
            }
            "fail" -> {
                ivIcon.setImageResource(R.drawable.icon_type_failed)
                tvTitle.text = "Send ${item.to_addr}"
                tvBalance.text = "-${item.value}${item.symbol}"
            }
            else -> {
                ivIcon.setImageResource(R.drawable.icon_type_failed)
                tvTitle.text = "Send ${item.to_addr}"
                tvBalance.text = "-${item.value}${item.symbol}"
            }
        }
        tvTime.text = item.time
        tvValue.text = "$${item.balance_u}"
    }
}