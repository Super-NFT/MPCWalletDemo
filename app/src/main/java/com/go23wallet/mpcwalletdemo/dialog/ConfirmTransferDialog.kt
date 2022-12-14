package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogConfirmTransferLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.TextEllipsizeSpanUtil

class ConfirmTransferDialog(private val mContext: Context) :
    BaseDialogFragment<DialogConfirmTransferLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_confirm_transfer_layout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }


    override fun initViews(v: View?) {

        val address = "qwertyuuoplkkjhgdsaasdsadfadsfdzx"
        TextEllipsizeSpanUtil.setTextEndImg(mContext, viewBinding.tvContact, address, R.drawable.icon_copy) {
            CopyUtils.copyText(mContext, address)
        }

        TextEllipsizeSpanUtil.setTextEndImg(mContext, viewBinding.tvFrom, address, R.drawable.icon_copy) {
            CopyUtils.copyText(mContext, address)
        }

        TextEllipsizeSpanUtil.setTextEndImg(mContext, viewBinding.tvTo, address, R.drawable.icon_copy) {
            CopyUtils.copyText(mContext, address)
        }

        TextEllipsizeSpanUtil.setTextEndImg(mContext, viewBinding.tvValue, address, R.drawable.icon_copy) {
            CopyUtils.copyText(mContext, address)
        }

        viewBinding.tvGasFree.text = ""
        viewBinding.tvGasFreeValue.text = ""

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvConfirm.setOnClickListener {

        }
    }
}