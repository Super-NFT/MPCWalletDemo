package com.go23wallet.mpcwalletdemo.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23.bean.transaction.Transaction
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.ext.parseAddress

class TokenTransactionsAdapter :
    BaseQuickAdapter<Transaction, BaseViewHolder>(R.layout.layout_token_type_item) {

    override fun convert(holder: BaseViewHolder, item: Transaction) {
        val ivIcon = holder.getView<AppCompatImageView>(R.id.iv_icon)
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val tvTime = holder.getView<AppCompatTextView>(R.id.tv_time)
        val tvBalance = holder.getView<AppCompatTextView>(R.id.tv_charge_balance)
        val tvValue = holder.getView<AppCompatTextView>(R.id.tv_value)
        tvTitle.text = "${item.type} ${item.to_addr.parseAddress()}"
        val valueStr = if (item.value.length > 12) {
            item.value.substring(0, 12) + "..."
        } else {
            item.value
        }
        tvBalance.setTextColor(context.getColor(R.color.color_D83548))
        tvBalance.text = "-${valueStr}${item.symbol}"
        tvValue.visibility =
            if (item.balance_u.toDouble() > 0) View.VISIBLE else View.INVISIBLE
        when (item.status) {
            3 -> {
                ivIcon.setImageResource(R.drawable.icon_type_failed)
                tvTitle.text = "${item.type} ${item.to_addr.parseAddress()}"
                if (item.type == "Approve") {
                    tvValue.visibility = View.INVISIBLE
                }
            }
            else -> {
                when (item.type) {
                    "Send" -> {
                        ivIcon.setImageResource(R.drawable.icon_type_send)
                    }
                    "Receive" -> {
                        tvBalance.setTextColor(context.getColor(R.color.color_00D6E1))
                        ivIcon.setImageResource(R.drawable.icon_type_receive)
                        tvTitle.text = "${item.type} ${item.from_addr.parseAddress()}"
                        tvBalance.text = "+${item.value}${item.symbol}"
                    }
                    "Approve" -> {
                        tvBalance.setTextColor(context.getColor(R.color.color_262626))
                        ivIcon.setImageResource(R.drawable.icon_type_approve)
                        tvBalance.text = "${item.value}${item.symbol}"
                        tvValue.visibility = View.INVISIBLE
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