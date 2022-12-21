package com.go23wallet.mpcwalletdemo.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSendCoinResultLayoutBinding
import com.go23wallet.mpcwalletdemo.wallet.ChargeDetailsActivity

class SendCoinResultDialog(private val mContext: Activity, private val isSuccess: Boolean, private val hash: String) :
    BaseDialogFragment<DialogSendCoinResultLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_send_coin_result_layout

    override fun initViews(v: View?) {
        if (isSuccess) {
            viewBinding.tvOk.visibility = View.GONE
        } else {
            viewBinding.ivStatus.setImageResource(R.drawable.icon_charge_failed)
            viewBinding.tvOk.visibility = View.VISIBLE
            viewBinding.tvConfirm.visibility = View.GONE
            viewBinding.tvDetails.visibility = View.GONE
            viewBinding.tvSuccessContent.visibility = View.GONE
        }

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvDetails.setOnClickListener {
            startActivity(Intent(mContext, ChargeDetailsActivity::class.java).apply {
                putExtra("hash", hash)
            })
        }

        viewBinding.tvConfirm.setOnClickListener {
            dismissAllowingStateLoss()
            mContext.finish()
        }
        viewBinding.tvOk.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}