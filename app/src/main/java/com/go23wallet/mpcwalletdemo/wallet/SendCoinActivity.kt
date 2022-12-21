package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.Sign
import com.coins.app.bean.transaction.PreTokenSend
import com.coins.app.bean.transaction.PreTokenSendResponse
import com.coins.app.manage.Go23WalletTransactionManage
import com.coins.app.ui.gamecenter.ApproveDialog
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendCoinBinding
import com.go23wallet.mpcwalletdemo.dialog.SelectTokenSendDialog
import com.go23wallet.mpcwalletdemo.dialog.SendCoinResultDialog
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.google.gson.Gson
import com.google.zxing.activity.CaptureActivity


class SendCoinActivity : BaseActivity<ActivitySendCoinBinding>() {

    private val selectTokenSendDialog: SelectTokenSendDialog by lazy {
        SelectTokenSendDialog(this)
    }

    override val layoutRes: Int = R.layout.activity_send_coin

    private var isSelectGas = true

    //    private var tokenId = 0
    private var chainTokenInfo: ChainTokenInfo? = null
    private var preTokenSend: PreTokenSend? = null

    private var sendCoinResultDialog: SendCoinResultDialog? = null

    override fun initViews(savedInstanceState: Bundle?) {
//        tokenId = intent.getIntExtra("token_id", 0)
        chainTokenInfo = intent.getParcelableExtra("data")
        if (chainTokenInfo == null) {
            finish()
            return
        }
        initData()
        setListener()
    }

    private fun initData() {
        chainTokenInfo?.let {
            GlideUtils.loadImg(this, it.imgUrl, binding.ivCoinIcon)
            binding.tvCoinName.text = it.name
            binding.tvFromCoinNickname.text = it.symbol
            Go23WalletTransactionManage.getInstance().requestPreTokenSend(
                UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
                it.contract_address,
                it.user_wallet_address,
                object : BaseCallBack<PreTokenSendResponse> {
                    override fun success(data: PreTokenSendResponse?) {
                        data?.data?.let { preToken ->
                            preTokenSend = preToken
                            binding.etToAddress.setText("")
                            binding.tvGasTips.text =
                                String.format(getString(R.string.gas_tips), chainTokenInfo?.symbol)
                            binding.tvGasTips.visibility =
                                if (preToken.isIs_lending_gas) View.VISIBLE else View.GONE
                            binding.tvFromAddress.text = it.user_wallet_address
                            binding.tvAvailable.text =
                                String.format(
                                    getString(R.string.available),
                                    "${if (it.contract_address.isEmpty()) preToken.platform_balance_sort else preToken.token_balance_sort} ${it.symbol}"
                                )
                            binding.tvCoinSymbol.text = it.symbol
                            binding.tvGasBalance.text = "${preToken.gas} ${it.symbol}"
                            binding.tvGasValue.text =
                                "=$${preToken.platform_u_per * (preToken.gas ?: "0.00").toDouble()}"
                        }
                    }

                    override fun failed() {
                    }
                })
        }
    }

    private fun setListener() {
        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val data = it.data?.getStringExtra("result") ?: ""
            binding.etToAddress.setText(data)
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        selectTokenSendDialog.callback = {
//            tokenId = it.token_id
            chainTokenInfo =
                ChainTokenInfo(
                    it.chain_id,
                    it.user_wallet_address,
                    it.name,
                    it.symbol,
                    it.image_url,
                    it.contract_address,
                )
            initData()
        }

        binding.vCoinType.setOnClickListener {
            selectTokenSendDialog.show(supportFragmentManager, "selectTokenSendDialog")
        }
        binding.etInputNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.tvTotalValue.text = ""
                    binding.tvInputValue.text = ""
                    binding.tvSend.isEnabled = false
                    return
                }
                val num = s.toString().toDouble()

                val uPer = if (chainTokenInfo?.contract_address.isNullOrEmpty()) {
                    preTokenSend?.platform_u_per
                } else {
                    preTokenSend?.token_u_per
                }
                binding.tvInputValue.text = if (uPer == null || uPer <= 0) "" else "=$${num * uPer}"

                updateSendStatus()
            }
        })
        binding.tvPaste.setOnClickListener {
            CopyUtils.pasteText(this, binding.etToAddress)
        }
        binding.tvAll.setOnClickListener {
            preTokenSend?.let {
                val availableNum =
                    if (chainTokenInfo?.contract_address.isNullOrEmpty()) it.platform_balance_sort else it.token_balance_sort
                val num = availableNum * (1 - it.fee)
                binding.etInputNum.setText("$num")
            }
        }
        binding.ivClear.setOnClickListener {
            binding.etInputNum.setText("0.00")
        }
        binding.ivScanCode.setOnClickListener {
            PermissionUtils.permission(PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        try {
                            registerResult.launch(
                                Intent(
                                    this@SendCoinActivity,
                                    CaptureActivity::class.java
                                )
                            )
                        } catch (ignored: Exception) {
                            Toast.makeText(
                                this@SendCoinActivity,
                                getString(R.string.request_camera_permission_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onDenied() {
                        Toast.makeText(
                            this@SendCoinActivity,
                            getString(R.string.request_camera_permission_fail),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }).request()
        }
        binding.tvGasTips.setOnClickListener {
            isSelectGas = !isSelectGas
            binding.tvGasTips.setCompoundDrawablesRelativeWithIntrinsicBounds(
                if (isSelectGas) R.drawable.icon_checked else R.drawable.icon_uncheck,
                0,
                0,
                0
            )
            updateSendStatus()
        }
        binding.tvSend.setOnClickListener {
            preTokenSend?.let {
                showProgress()
                toSign(it)
            }
        }
    }

    private fun toSign(data: PreTokenSend) {
        val sign = Sign()
        val key1 = Go23WalletManage.getInstance()
            .getLocalMpcKey(Go23WalletManage.getInstance().walletAddress)
        sign.type = 1
        sign.chainId = UserWalletInfoManager.getUserWalletInfo().userChain.chain_id.toString()
        sign.rpc = UserWalletInfoManager.getUserWalletInfo().userChain.rpc.toString()
        sign.fromAddr = chainTokenInfo?.user_wallet_address
        sign.toAddr = binding.etToAddress.text.toString()
        sign.transType = data.trans_type
        sign.contractAddress = chainTokenInfo?.contract_address
        sign.tokenId = ""
        sign.value = binding.etInputNum.text.toString()
        sign.middleContractAddress = UserWalletInfoManager.getUserWalletInfo().userChain.middle_contract_address
        if (data.trans_type == 4) {
            Go23WalletManage.getInstance().showApproveDialog(
                this,
                supportFragmentManager,
                "approveDialog",
                sign,
                object : ApproveDialog.CallBack {
                    override fun approve() {
                        Go23WalletManage.getInstance().sign(
                            key1, Gson().toJson(sign).toByteArray()
                        ) { response ->
                            dismissProgress()
                            sendCoinResultDialog =
                                SendCoinResultDialog(this@SendCoinActivity, true, response.data)
                            sendCoinResultDialog?.show(
                                supportFragmentManager,
                                "sendCoinResultDialog"
                            )
                        }
                    }

                    override fun cancel() {
                    }
                })
        } else {
            Go23WalletManage.getInstance().sign(
                key1, Gson().toJson(sign).toByteArray()
            ) { response ->
                dismissProgress()
                if (response.code.toString() == "0") {
                    sendCoinResultDialog = SendCoinResultDialog(this, true, response.data ?: "")
                    sendCoinResultDialog?.show(supportFragmentManager, "sendCoinResultDialog")
                } else {
                    Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateSendStatus() {
        val inputStr = binding.etInputNum.text.toString()
        if (inputStr.isNullOrEmpty()) {
            return
        }
        val num = inputStr.toDouble()
        var totalValue = 1.0
        var availableNum = 0.0
        preTokenSend?.let {
            totalValue = num * (1 + it.fee)
            availableNum =
                if (chainTokenInfo?.contract_address.isNullOrEmpty()) it.platform_balance_sort else it.token_balance_sort
        }
        binding.tvTotalValue.text = "$totalValue ${chainTokenInfo?.symbol}"
        binding.tvSend.isEnabled = totalValue > 0 && totalValue <= availableNum && isSelectGas
    }
}