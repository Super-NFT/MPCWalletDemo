package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogReceiveLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.ScreenUtils
import com.go23wallet.mpcwalletdemo.utils.TextEllipsizeSpanUtil


class ReceiveDialog(private val mContext: Context, private val mData: String) : BaseDialogFragment<DialogReceiveLayoutBinding>() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setCanceledOnTouchOutside(true)
        isCancelable = true
        setWidth((ScreenUtils.getScreenWidth(context) * 0.8).toInt())
        setGravity(Gravity.CENTER)
    }

    override val layoutId: Int = R.layout.dialog_receive_layout

    override fun initViews(v: View?) {
        viewBinding.tvTitle.text = mData
        viewBinding.tvContent.text = String.format(getString(R.string.receive_tips), mData)
        GlideUtils.loadImg(mContext, mData, viewBinding.ivQrcode)
        val address = "qwertyuuoplkkjhgdsaasdsadfadsfdzx"
        TextEllipsizeSpanUtil.setTextEndImg(mContext, viewBinding.tvAddress, address, R.drawable.icon_copy) {
            CopyUtils.copyText(mContext, address)
        }
    }
}