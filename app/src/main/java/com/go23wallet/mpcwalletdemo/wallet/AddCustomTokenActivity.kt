package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddCustomTokenBinding

class AddCustomTokenActivity : BaseActivity<ActivityAddCustomTokenBinding>() {

    private var isInput = false


    override val layoutRes: Int = R.layout.activity_add_custom_token

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        setListener()
    }

    private fun initView() {

    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.etTokenContact.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
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