package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.nft.Nft
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class NFTAdapter(private val mContext: Context) :
    BaseQuickAdapter<Nft, BaseViewHolder>(R.layout.layout_nft_item) {

    override fun convert(holder: BaseViewHolder, item: Nft) {
        val ivImg = holder.getView<AppCompatImageView>(R.id.iv_img)
        val tvNum = holder.getView<AppCompatTextView>(R.id.tv_num)
        val tvName = holder.getView<AppCompatTextView>(R.id.tv_name)
        GlideUtils.loadRound(mContext, item.image, ivImg, 8)
        tvNum.visibility = if (item.value > 1) View.VISIBLE else View.GONE
        tvNum.text = "x${item.value}"
        tvName.text = item.name

    }
}