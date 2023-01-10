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

class ReceiveDialog(
    private val mContext: Context,
    private val symbol: String,
    private val address: String
) : BaseDialogFragment<DialogReceiveLayoutBinding>() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setCanceledOnTouchOutside(true)
        isCancelable = true
        setWidth((ScreenUtils.getScreenWidth() * 0.9).toInt())
        setGravity(Gravity.CENTER)
    }

    override val layoutId: Int = R.layout.dialog_receive_layout

    override fun initViews(v: View?) {
        viewBinding.tvTitle.text = symbol
        viewBinding.tvBottomTips.text =
            String.format(getString(R.string.receive_qrcode_tips), symbol)
        viewBinding.ivQrcode.setImageBitmap(BitmapUtils.create2DCode(address))

        val newText = if (address.length > 30) {
            address.substring(0, 30) + "\n" + address.substring(30)
        } else {
            address
        }
        TextEllipsizeSpanUtil.setTextEndImg(
            mContext,
            viewBinding.tvAddress,
            newText,
            R.drawable.icon_copy
        )
        viewBinding.tvAddress.setOnClickListener {
            CopyUtils.copyText(mContext, address)

        }
    }
}