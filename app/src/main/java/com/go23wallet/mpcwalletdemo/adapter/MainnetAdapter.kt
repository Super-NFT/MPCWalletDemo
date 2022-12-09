package com.go23wallet.mpcwalletdemo.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class MainnetAdapter(val id: String) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_mainnet_layout) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val ivAvatar = holder.getView<AppCompatImageView>(R.id.iv_avatar)
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val ivSelect = holder.getView<AppCompatImageView>(R.id.iv_select)
        GlideUtils.loadImg(context, item, ivAvatar)
        tvTitle.text = item
        ivSelect.visibility = if (id == item) View.VISIBLE else View.GONE
    }

}