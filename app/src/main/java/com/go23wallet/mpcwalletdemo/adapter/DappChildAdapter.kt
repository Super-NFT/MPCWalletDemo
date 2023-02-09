package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.data.DappItem
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class DappChildAdapter(private val mContext: Context) :
    BaseQuickAdapter<DappItem, BaseViewHolder>(R.layout.layout_app_child_item) {

    override fun convert(holder: BaseViewHolder, item: DappItem) {
        val ivImg = holder.getView<AppCompatImageView>(R.id.iv_img)
        val tvName = holder.getView<AppCompatTextView>(R.id.tv_name)
        val tvContent = holder.getView<AppCompatTextView>(R.id.tv_content)
        val ivChain = holder.getView<AppCompatImageView>(R.id.iv_chain)
        GlideUtils.loadImg(mContext, item.icon, ivImg)
        GlideUtils.loadImg(mContext, item.chainIcon, ivChain)
        tvName.text = item.title
        tvContent.text = item.content

    }
}