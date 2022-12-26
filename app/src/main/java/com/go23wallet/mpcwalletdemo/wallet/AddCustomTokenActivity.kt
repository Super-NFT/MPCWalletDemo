package com.go23wallet.mpcwalletdemo.wallet

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.token.TokenResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddCustomTokenBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager

class AddCustomTokenActivity : BaseActivity<ActivityAddCustomTokenBinding>() {

    override val layoutRes: Int = R.layout.activity_add_custom_token

    private var contractAddress = ""

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
        binding.etTokenContact.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val address = s?.toString() ?: return
                if (address.length >= 30) {
                    checkTokenAddress(address)
                }
            }
        })
        binding.tvConfirm.setOnClickListener {
            showProgress()
            Go23WalletManage.getInstance().addToken(
                contractAddress,
                UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
                object : BaseCallBack<TokenResponse> {
                    override fun success(data: TokenResponse?) {
                        dismissProgress()
                        val token = data?.data ?: return
                        UpdateDataLiveData.liveData.postValue(1)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun failed() {
                        dismissProgress()
                    }
                })
        }
    }

    private fun checkTokenAddress(address: String) {
        Go23WalletManage.getInstance().checkToken(address,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            object : BaseCallBack<TokenResponse> {
                override fun success(data: TokenResponse?) {
                    data?.data?.let {
                        contractAddress = it.contract_address ?: ""
                        binding.tvTokenSymbol.text = it.symbol
                        binding.tvTokenPrecision.text = it.decimal.toString()
                    } ?: kotlin.run {
                        binding.tvTokenSymbol.text = ""
                        binding.tvTokenPrecision.text = ""
                    }
                }

                override fun failed() {
                }
            })
    }
}