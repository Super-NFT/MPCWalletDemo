package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class AddTokenListAdapter(private val mContext: Context): BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_add_token_layout) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val ivTokenIcon = holder.getView<AppCompatImageView>(R.id.iv_token_icon)
        val tvTokenName = holder.getView<AppCompatTextView>(R.id.tv_token_name)
        val tvTokenNum = holder.getView<AppCompatTextView>(R.id.tv_token_num)
        val tvTokenValue = holder.getView<AppCompatTextView>(R.id.tv_token_value)
        val ivStatus = holder.getView<AppCompatImageView>(R.id.iv_status)
        GlideUtils.loadImg(mContext, item, ivTokenIcon)
        tvTokenName.text = item
        tvTokenNum.text = item
        tvTokenValue.text = item
        ivStatus.setImageResource(R.drawable.icon_checked)
    }
}