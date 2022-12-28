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
        tvTitle.text = "${item.type} ${item.to_addr}"
        tvBalance.setTextColor(context.getColor(R.color.color_262626))
        tvBalance.text = "-${item.value}${item.symbol}"
        when(item.status){
            3 -> {
                ivIcon.setImageResource(R.drawable.icon_type_failed)
                tvTitle.text = "${item.type} ${item.to_addr}"
            }
            else -> {
                when (item.type) {
                    "Send" -> {
                        ivIcon.setImageResource(R.drawable.icon_type_send)
                    }
                    "Receive" -> {
                        tvBalance.setTextColor(context.getColor(R.color.color_35C1D8))
                        ivIcon.setImageResource(R.drawable.icon_type_receive)
                        tvTitle.text = "${item.type} ${item.from_addr}"
                        tvBalance.text = "+${item.value}${item.symbol}"
                    }
                    "Approve" -> {
                        ivIcon.setImageResource(R.drawable.icon_type_approve)
                    }
                    "Mint" -> {
                        ivIcon.setImageResource(R.drawable.icon_type_transfer)
                    }
                    "Swap" -> {
                        ivIcon.setImageResource(R.drawable.icon_type_transfer)
                    }
                    else -> {
                        ivIcon.setImageResource(R.drawable.icon_type_transfer)
                    }
                }
             }
        }

        tvTime.text = item.time
        tvValue.text = "$${item.balance_u}"
    }
}