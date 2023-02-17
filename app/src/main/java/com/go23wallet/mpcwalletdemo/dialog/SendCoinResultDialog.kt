package com.go23wallet.mpcwalletdemo.dialog

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.*
import com.Go23WalletManage
import com.go23.bean.transaction.TransactionDetailResponse
import com.go23.callback.BaseCallBack
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSendCoinResultLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.ChargeDetailsActivity

class SendCoinResultDialog(
    private val mContext: Activity,
    private val isSuccess: Boolean,
    private val hash: String,
    private val isNft: Boolean = false
) :
    BaseDialogFragment<DialogSendCoinResultLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_send_coin_result_layout

    private val mHandler = Handler(Looper.getMainLooper())

    override fun initViews(v: View?) {
        initData()
        if (!isSuccess) {
            viewBinding.ivStatus.setImageResource(R.drawable.icon_charge_failed)
            viewBinding.tvDetails.visibility = View.GONE
            viewBinding.tvSuccessContent.visibility = View.GONE
            viewBinding.tvStatus.text = getString(R.string.lite_failed)
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
            if (isSuccess) {
                mContext.finish()
                if (isNft) {
                    UpdateDataLiveData.setUpdateType(2)
                } else {
                    UpdateDataLiveData.setUpdateType(3)
                }
            }
        }
    }

    private fun initData() {
        Go23WalletManage.getInstance().requestTransactionDetail(
            hash,
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            object : BaseCallBack<TransactionDetailResponse> {
                override fun success(data: TransactionDetailResponse?) {
                    data?.data?.let {
                        when (it.status) {
                            1 -> {
                                mHandler.postDelayed(runnable, 3 * 1000)
                            }
                            2 -> {
                                viewBinding.ivStatus.setImageResource(R.drawable.icon_charge_success)
                            }
                            else -> {
                                viewBinding.ivStatus.setImageResource(R.drawable.icon_charge_failed)
                            }
                        }
                    }
                }

                override fun failed() {
                }
            })
    }

    private val runnable = Runnable {
        initData()
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        mHandler.removeCallbacks(runnable)
    }
}