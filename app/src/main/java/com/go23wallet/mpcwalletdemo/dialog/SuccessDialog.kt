package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSuccessLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.ScreenUtils

class SuccessDialog(private val mContext: Context, private val title: String) :
    BaseDialogFragment<DialogSuccessLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_success_layout


    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight(context) * 0.8).toInt())
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
}