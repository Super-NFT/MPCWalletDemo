package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogTransactionResultLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.ScreenUtils


class TransactionResultDialog(private val mContext: Context, private val mData: String) :
    BaseDialogFragment<DialogTransactionResultLayoutBinding>() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight(mContext) * 0.8).toInt())
        setGravity(Gravity.BOTTOM)
    }

    override val layoutId: Int = R.layout.dialog_transaction_result_layout

    override fun initViews(v: View?) {

        viewBinding.ivStatus.setImageResource(R.drawable.icon_charge_success)
        viewBinding.tvStatus.text = mData

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvConfirm.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}