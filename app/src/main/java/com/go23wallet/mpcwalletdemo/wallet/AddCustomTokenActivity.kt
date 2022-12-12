package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddCustomTokenBinding

class AddCustomTokenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCustomTokenBinding

    private var isInput = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCustomTokenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {

    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvConfirm.setOnClickListener {
            if (isInput) {
                isInput = false
                binding.etTokenContact.visibility = View.GONE
                binding.tvTokenSymbol.text = ""
                binding.tvTokenPrecision.text = ""
            } else {
                // TODO
                finish()
            }
        }
    }
}