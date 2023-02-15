package com.go23wallet.mpcwalletdemo.adapter

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.go23.bean.nft.Nft
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.view.RoundImageView

class NFTAdapter(private val mContext: Context) :
    BaseQuickAdapter<Nft, BaseViewHolder>(R.layout.layout_nft_item) {

    override fun convert(holder: BaseViewHolder, item: Nft) {
        val ivImg = holder.getView<RoundImageView>(R.id.iv_img)
        val tvNum = holder.getView<AppCompatTextView>(R.id.tv_num)
        val tvName = holder.getView<AppCompatTextView>(R.id.tv_name)
        GlideUtils.loadImg(mContext, item.image, ivImg)
        tvNum.visibility = if (item.value > 1) View.VISIBLE else View.GONE
        tvNum.text = "x${item.value}"
        tvName.text = item.name

    }
}