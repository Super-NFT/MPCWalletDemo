package com.go23wallet.mpcwallet.dialog

import android.content.Context
import android.view.*
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwallet.R
import com.go23wallet.mpcwallet.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwallet.databinding.DialogSuccessLayoutBinding

class SuccessDialog(private val mContext: Context, private val title: String) :
    BaseDialogFragment<DialogSuccessLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_success_layout

    var callback: () -> Unit = {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        viewBinding.tvTitle.text = title
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvConfirm.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        callback.invoke()
    }
}