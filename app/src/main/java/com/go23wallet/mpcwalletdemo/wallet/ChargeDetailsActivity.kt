package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.databinding.ActivityChargeDetailsBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils

class ChargeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChargeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargeDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

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
        binding.tvTotalValue.text = ""
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