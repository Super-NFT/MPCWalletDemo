package com.go23wallet.mpcwalletdemo.adapter

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.nft.Nft
import com.go23wallet.mpcwalletdemo.R

class AreaCodeAdapter : BaseQuickAdapter<Nft, BaseViewHolder>(R.layout.layout_area_code) {

    override fun convert(holder: BaseViewHolder, item: Nft) {
        val tvName = holder.getView<AppCompatTextView>(R.id.tv_name)
        val tvCode = holder.getView<AppCompatTextView>(R.id.tv_code)
        tvName.text = ""
        tvCode.text = ""
    }
}