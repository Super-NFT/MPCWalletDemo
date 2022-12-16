package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityChargeDetailsBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils

class ChargeDetailsActivity : BaseActivity<ActivityChargeDetailsBinding>() {

    override val layoutRes: Int = R.layout.activity_charge_details

    private var transactionId = 0

    override fun initViews(savedInstanceState: Bundle?) {
        transactionId = intent.getIntExtra("transaction_id", 0)
        initView()
        setListener()
    }

    private fun initView() {
        binding.ivChargeType.setImageResource(R.drawable.icon_charge_processing)
        binding.tvType.text = ""
        binding.tvTime.text = ""
        binding.tvFromAddress.text = ""
        binding.tvToAddress.text = ""
        binding.tvTxIdAddress.text = ""
        binding.tvNetworkContent.text = ""
        binding.tvAmountValue.text = ""
        binding.tvGasValue.text = ""
//        binding.tvTotalValue.text = ""
    }

    private fun setListener() {
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