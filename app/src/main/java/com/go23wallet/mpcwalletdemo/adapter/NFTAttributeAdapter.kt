package com.go23wallet.mpcwalletdemo.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23.bean.nft.NftAttribute
import com.go23wallet.mpcwalletdemo.R

class NFTAttributeAdapter :
    BaseQuickAdapter<NftAttribute, BaseViewHolder>(R.layout.item_nft_attribute_layout) {

    override fun convert(holder: BaseViewHolder, item: NftAttribute) {
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val tvContent = holder.getView<AppCompatTextView>(R.id.tv_content)
        tvTitle.text = item.trait_type
        tvContent.text = item.value
    }

}