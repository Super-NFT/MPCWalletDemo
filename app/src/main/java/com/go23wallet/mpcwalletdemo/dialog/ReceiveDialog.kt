package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogReceiveLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.TextEllipsizeSpanUtil
import com.google.zxing.common.BitmapUtils


class ReceiveDialog(private val mContext: Context, private val mData: String, private val address: String) : BaseDialogFragment<DialogReceiveLayoutBinding>() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setCanceledOnTouchOutside(true)
        isCancelable = true
        setWidth((ScreenUtils.getScreenWidth() * 0.8).toInt())
        setGravity(Gravity.CENTER)
    }

    override val layoutId: Int = R.layout.dialog_receive_layout

    override fun initViews(v: View?) {
        viewBinding.tvTitle.text = mData
        viewBinding.tvBottomTips.text = String.format(getString(R.string.receive_qrcode_tips), mData)
        viewBinding.ivQrcode.setImageBitmap(BitmapUtils.create2DCode(address))
        TextEllipsizeSpanUtil.setTextEndImg(mContext, viewBinding.tvAddress, address, R.drawable.icon_copy)
        viewBinding.tvAddress.setOnClickListener {
            CopyUtils.copyText(mContext, address)

        }
    }
}