package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletTokensManage
import com.coins.app.bean.token.TokenResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddCustomTokenBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager

class AddCustomTokenActivity : BaseActivity<ActivityAddCustomTokenBinding>() {

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
            UpdateDataLiveData.setUpdateType(1)
        }
    }

    private fun checkTokenAddress(address: String) {
        Go23WalletTokensManage.getInstance().checkToken(address,
            UserWalletInfoManager.getUserWalletInfo().userChainId,
            object : BaseCallBack<TokenResponse> {
                override fun success(data: TokenResponse?) {
                    data?.data?.let {
                        binding.tvTokenSymbol.text = it.symbol
                        binding.tvTokenPrecision.text = it.decimal.toString()
                    }
                }

                override fun failed() {
                }
            })
    }
}