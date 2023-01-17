package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.transaction.TransactionDetail
import com.coins.app.bean.transaction.TransactionDetailResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityChargeDetailsBinding
import com.go23wallet.mpcwalletdemo.ext.parseAddress
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.TextEllipsizeSpanUtil
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager

class ChargeDetailsActivity : BaseActivity<ActivityChargeDetailsBinding>() {

    override val layoutRes: Int = R.layout.activity_charge_details

    private var hash = ""

    private var details: TransactionDetail? = null

    private val mHandler = Handler(Looper.getMainLooper())

    override fun initViews(savedInstanceState: Bundle?) {
        hash = intent.getStringExtra("hash") ?: ""
        initData()
        setListener()
    }

    private fun initData() {
        Go23WalletManage.getInstance().requestTransactionDetail(
            hash,
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            object : BaseCallBack<TransactionDetailResponse> {
                override fun success(data: TransactionDetailResponse?) {
                    details = data?.data
                    initView()
                }

                override fun failed() {
                }
            })
    }

    private fun initView() {
        details?.let {
            if (it.transaction_class == 3) { // nft
                binding.nftView.visibility = View.VISIBLE
                GlideUtils.loadImg(this@ChargeDetailsActivity, it.image, binding.ivNft)
                binding.tvNftName.text = it.image_name
                TextEllipsizeSpanUtil.setTextEndImg(
                    this@ChargeDetailsActivity,
                    binding.tvTokenId,
                    it.token,
                    R.drawable.icon_copy
                )
                binding.tvAmountValue.visibility = View.GONE
                binding.tvAmount.visibility = View.GONE
            } else {
                binding.nftView.visibility = View.GONE
                binding.tvAmountValue.text = "${it.amount} ${it.symbol}"
            }
            when (it.status) {
                1 -> {
                    binding.ivChargeType.setImageResource(R.drawable.icon_charge_processing)
                    binding.tvType.text = getString(R.string.processing)
                    mHandler.postDelayed(runnable, 3 * 1000)
                }
                2 -> {
                    binding.ivChargeType.setImageResource(R.drawable.icon_charge_success)
                    binding.tvType.text = getString(R.string.successful)
                }
                else -> {
                    binding.ivChargeType.setImageResource(R.drawable.icon_charge_failed)
                    binding.tvType.text = getString(R.string.failed)
                }
            }
            if (it.lending_gas_fee.isNullOrEmpty() || it.lending_gas_fee == "0") {
                binding.vLending.visibility = View.GONE
                binding.tvLending.visibility = View.GONE
                binding.tvLendingContent.visibility = View.GONE
            } else {
                binding.vLending.visibility = View.VISIBLE
                binding.tvLending.visibility = View.VISIBLE
                binding.tvLendingContent.visibility = View.VISIBLE
                binding.tvLendingContent.text = "${it.lending_gas_fee}${it.symbol}"
            }
            binding.tvTime.visibility = if (it.status == 1) View.INVISIBLE else View.VISIBLE
            binding.tvTime.text = it.time
            TextEllipsizeSpanUtil.setTextEndImg(
                this,
                binding.tvFromAddress,
                it.from_addr.parseAddress(),
                R.drawable.icon_copy
            )
            TextEllipsizeSpanUtil.setTextEndImg(
                this,
                binding.tvToAddress,
                it.to_addr.parseAddress(),
                R.drawable.icon_copy
            )
            TextEllipsizeSpanUtil.setTextEndImg(
                this,
                binding.tvTxIdAddress,
                it.hash.parseAddress(),
                R.drawable.icon_copy
            )
            binding.tvNetworkContent.text = it.network
            binding.tvGasValue.text = "${it.gas_fee} ${it.gas_symbol}"
        }
    }

    private val runnable = Runnable {
        initData()
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvTokenId.setOnClickListener {
            CopyUtils.copyText(this, details?.token ?: "")
        }
        binding.tvFromAddress.setOnClickListener {
            CopyUtils.copyText(this, details?.from_addr ?: "")
        }
        binding.tvToAddress.setOnClickListener {
            CopyUtils.copyText(this, details?.to_addr ?: "")
        }
        binding.tvTxIdAddress.setOnClickListener {
            CopyUtils.copyText(this, details?.hash ?: "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(runnable)
    }
}