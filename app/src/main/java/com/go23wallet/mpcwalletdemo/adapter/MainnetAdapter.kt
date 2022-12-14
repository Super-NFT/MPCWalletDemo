package com.go23wallet.mpcwalletdemo.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.coins.app.bean.chain.UserChain
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class MainnetAdapter :
    BaseQuickAdapter<UserChain, BaseViewHolder>(R.layout.item_mainnet_layout) {

    override fun convert(holder: BaseViewHolder, item: UserChain) {
        val ivAvatar = holder.getView<AppCompatImageView>(R.id.iv_avatar)
        val tvTitle = holder.getView<AppCompatTextView>(R.id.tv_title)
        val ivSelect = holder.getView<AppCompatImageView>(R.id.iv_select)
        GlideUtils.loadImg(context, item.image_url, ivAvatar)
        tvTitle.text = item.name
        ivSelect.visibility = if (item.has_default == 1) View.VISIBLE else View.GONE
    }

}