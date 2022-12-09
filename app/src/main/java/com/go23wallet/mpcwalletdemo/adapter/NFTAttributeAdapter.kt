package com.go23wallet.mpcwalletdemo.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23wallet.mpcwalletdemo.R

class NFTAttributeAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_nft_attribute_layout) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val tvContent = holder.getView<AppCompatTextView>(R.id.tv_content)
        tvTitle.text = item
        tvContent.text = item
    }

}