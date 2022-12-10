package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.content.Intent
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.MainnetAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogChooseMainnetLayoutBinding
import com.go23wallet.mpcwalletdemo.databinding.DialogConfirmTransferLayoutBinding
import com.go23wallet.mpcwalletdemo.databinding.DialogSendCoinResultLayoutBinding
import com.go23wallet.mpcwalletdemo.databinding.DialogSettingLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.ScreenUtils
import com.go23wallet.mpcwalletdemo.utils.TextEllipsizeSpanUtil
import com.go23wallet.mpcwalletdemo.wallet.ChargeDetailsActivity

class SendCoinResultDialog(private val mContext: Context) :
    BaseDialogFragment<DialogSendCoinResultLayoutBinding>() {

    private var isSuccess = true

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
                putExtra("id", "")
            })
        }

        viewBinding.tvConfirm.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.tvOk.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}