package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogTransactionResultLayoutBinding

class TransactionResultDialog(private val mContext: Context) :
    BaseDialogFragment<DialogTransactionResultLayoutBinding>() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
        setGravity(Gravity.BOTTOM)
    }

    override val layoutId: Int = R.layout.dialog_transaction_result_layout

    override fun initViews(v: View?) {

        viewBinding.ivStatus.setImageResource(R.drawable.icon_charge_success)
        viewBinding.tvStatus.text = ""

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvConfirm.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}