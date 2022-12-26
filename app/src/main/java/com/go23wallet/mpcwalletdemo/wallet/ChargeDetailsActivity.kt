package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import android.view.View
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.transaction.TransactionDetailResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityChargeDetailsBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class ChargeDetailsActivity : BaseActivity<ActivityChargeDetailsBinding>() {

    override val layoutRes: Int = R.layout.activity_charge_details

    private var hash = ""

    override fun initViews(savedInstanceState: Bundle?) {
        hash = intent.getStringExtra("hash") ?: ""
        initView()
        setListener()
    }

    private fun initView() {
        Go23WalletManage.getInstance().requestTransactionDetail(
            hash,
            object : BaseCallBack<TransactionDetailResponse> {
                override fun success(data: TransactionDetailResponse?) {
                    data?.data?.let {
                        if (it.type == 3) {
                            binding.nftView.visibility = View.VISIBLE
                            GlideUtils.loadImg(this@ChargeDetailsActivity, it.image, binding.ivNft)
                            binding.tvNftName.text = it.image_name
                            binding.tvTokenId.text = it.token
                            binding.tvAmountValue.visibility = View.GONE
                            binding.tvAmount.visibility = View.GONE
                        } else {
                            binding.nftView.visibility = View.GONE
                            binding.tvAmountValue.text = "${it.amount}${it.symbol}"
                        }
                        when (it.status) {
                            1 -> {
                                binding.ivChargeType.setImageResource(R.drawable.icon_charge_processing)
                                binding.tvType.text = getString(R.string.processing)
                            }
                            2 -> {
                                binding.ivChargeType.setImageResource(R.drawable.icon_charge_success)
                                binding.tvType.text = getString(R.string.successfully)
                            }
                            else -> {
                                binding.ivChargeType.setImageResource(R.drawable.icon_charge_failed)
                                binding.tvType.text = getString(R.string.failed)
                            }
                        }
                        binding.tvTime.text = it.time
                        binding.tvFromAddress.text = it.from_addr
                        binding.tvToAddress.text = it.to_addr
                        binding.tvTxIdAddress.text = it.hash
                        binding.tvNetworkContent.text = it.network
                        binding.tvGasValue.text = "${it.gas_fee}${it.symbol}"
                    }
                }

                override fun failed() {
                }
            })
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivFromCopy.setOnClickListener {
            CopyUtils.copyText(this, binding.tvFromAddress.text.toString())
        }
        binding.ivToCopy.setOnClickListener {
            CopyUtils.copyText(this, binding.tvToAddress.text.toString())
        }
        binding.ivTxIdCopy.setOnClickListener {
            CopyUtils.copyText(this, binding.tvTxIdAddress.text.toString())
        }
    }
}