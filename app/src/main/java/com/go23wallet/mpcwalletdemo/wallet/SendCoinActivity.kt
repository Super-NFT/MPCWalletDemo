package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.Sign
import com.coins.app.bean.transaction.PreTokenSend
import com.coins.app.bean.transaction.PreTokenSendResponse
import com.coins.app.entity.mpc.SignResponse
import com.coins.app.util.MpcUtil
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendCoinBinding
import com.go23wallet.mpcwalletdemo.dialog.SelectTokenSendDialog
import com.go23wallet.mpcwalletdemo.dialog.SendCoinResultDialog
import com.go23wallet.mpcwalletdemo.ext.parseAddress
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.*
import com.google.zxing.activity.CaptureActivity
import java.math.BigDecimal
import java.text.DecimalFormat


class SendCoinActivity : BaseActivity<ActivitySendCoinBinding>() {

    private val selectTokenSendDialog: SelectTokenSendDialog by lazy {
        SelectTokenSendDialog(this)
    }

    private val format = DecimalFormat().apply {
        isParseBigDecimal = true
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
        binding.etInputNum.filters = arrayOf<InputFilter>(FloatLengthFilter(8))
        chainTokenInfo?.let {
            GlideUtils.loadImg(this, it.imgUrl, binding.ivCoinIcon)
            binding.tvCoinName.text = it.name
            binding.tvFromCoinNickname.text = it.symbol
            showProgress()
            Go23WalletManage.getInstance()
                .requestPreTokenSend(UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
                    it.contract_address,
                    it.user_wallet_address,
                    object : BaseCallBack<PreTokenSendResponse> {
                        override fun success(data: PreTokenSendResponse?) {
                            dismissProgress()
                            data?.let { msg ->
                                if (msg.code != 0) {
                                    CustomToast.showShort(R.string.send_error)
                                    return@let
                                }
                                msg.data?.let { preToken ->
                                    preTokenSend = preToken
                                    binding.etToAddress.setText("")
                                    binding.tvGasTips.text = String.format(
                                        getString(R.string.gas_tips), chainTokenInfo?.symbol
                                    )
                                    binding.tvMinTipsMiddle.text = preToken.token_minimum
                                    binding.tvMinTipsEnd.text = chainTokenInfo?.symbol
                                    binding.groupTips.visibility =
                                        if (preToken.isIs_lending_gas) View.VISIBLE else View.GONE
                                    binding.tvFromAddress.text =
                                        it.user_wallet_address.parseAddress()
                                    binding.tvAvailable.text = String.format(
                                        getString(R.string.available), "${
                                            if (it.contract_address.isEmpty()) format.parse(
                                                preToken.platform_balance_sort.toString()
                                            )
                                            else format.parse(preToken.token_balance_sort.toString())
                                        } ${it.symbol}"
                                    )
                                    binding.tvCoinSymbol.text = it.symbol
                                    val uPer =
                                        if (chainTokenInfo?.contract_address.isNullOrEmpty()) {
                                            preTokenSend?.platform_u_per?.toDouble() ?: 0.0
                                        } else {
                                            preTokenSend?.token_u_per?.toDouble() ?: 0.0
                                        }
                                    binding.tvInputValue.visibility =
                                        if (uPer > 0) View.VISIBLE else View.GONE
                                    binding.tvGasBalance.text =
                                        "${preToken.gas} ${UserWalletInfoManager.getUserWalletInfo().userChain.symbol}"
                                    binding.tvGasValue.visibility =
                                        if (preToken.platform_u_per.toDouble() > 0) View.VISIBLE else View.GONE
                                    binding.tvGasValue.text =
                                        "$${preToken.platform_u_per.toDouble() * (preToken.gas ?: "0.00").toDouble()}"
                                }
                            }
                        }

                        override fun failed() {
                            dismissProgress()
                        }
                    })
        }
    }

    private var isClickAll = false

    private fun setListener() {
        UpdateDataLiveData.liveData.observe(this) {
            if (it == 2) {
                finish()
            }
        }
        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val data = it.data?.getStringExtra("result") ?: ""
            val content = if (data.contains(":")) {
                if (data.contains("@")) {
                    data.split(":")[1].split("@")[0]
                } else {
                    data.split(":")[1]
                }
            } else {
                if (data.contains("@")) {
                    data.split("@")[0]
                } else {
                    data
                }
            }
            if (!content.isNullOrEmpty()) {
                binding.etToAddress.setText(content)
            }
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        selectTokenSendDialog.callback = {
//            tokenId = it.token_id
            chainTokenInfo = ChainTokenInfo(
                it.chain_id,
                it.user_wallet_address,
                it.name,
                it.symbol,
                it.image_url,
                it.contract_address,
            )
            initData()
        }

        binding.tvFromAddress.setOnClickListener {
            CopyUtils.copyText(this, chainTokenInfo?.user_wallet_address ?: "")
        }
        binding.vCoinType.setOnClickListener {
            selectTokenSendDialog.show(supportFragmentManager, "selectTokenSendDialog")
        }
        binding.etInputNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isClickAll = !binding.etInputNum.hasFocus()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
//                    binding.tvTotalValue.text = ""
                    binding.tvInputValue.text = "$0.00"
                    binding.tvSend.isEnabled = false
                    return
                }
                if (s.toString() == ".") {
                    binding.etInputNum.setText("0.")
                    binding.etInputNum.setSelection(2)
                    return
                }
                val num = s.toString().toDouble()

                val uPer = if (chainTokenInfo?.contract_address.isNullOrEmpty()) {
                    preTokenSend?.platform_u_per?.toDouble() ?: 0.0
                } else {
                    preTokenSend?.token_u_per?.toDouble() ?: 0.0
                }
                binding.tvInputValue.text =
                    if (uPer == null || uPer <= 0) "$0.00" else "$${num * uPer}"

                updateSendStatus(isClickAll)
            }
        })

        binding.etToAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                updateSendStatus(isClickAll)
            }
        })
        binding.tvPaste.setOnClickListener {
            CopyUtils.pasteText(this, binding.etToAddress)
        }
        binding.tvAll.setOnClickListener {
            preTokenSend?.let { preToken ->
                val availableNum = if (chainTokenInfo?.contract_address.isNullOrEmpty()) {
                    format.parse(
                        preToken.platform_balance_sort.toString()
                    ) as BigDecimal - BigDecimal(preToken.gas)
                } else format.parse(preToken.token_balance_sort.toString()) as? BigDecimal
                    ?: BigDecimal(0)
                binding.etInputNum.clearFocus()
                binding.etInputNum.setText(
                    "${
                        if (availableNum < BigDecimal(0)) {
                            CustomToast.showShort(R.string.gas_insufficient)
                            "0"
                        } else availableNum
                    }"
                )
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
                                    this@SendCoinActivity, CaptureActivity::class.java
                                )
                            )
                        } catch (ignored: Exception) {
                            CustomToast.showShort(R.string.request_camera_permission_fail)
                        }
                    }

                    override fun onDenied() {
                        CustomToast.showShort(R.string.request_camera_permission_fail)
                    }
                }).request()
        }
        binding.tvGasTips.setOnClickListener {
            isSelectGas = !isSelectGas
            binding.tvGasTips.setCompoundDrawablesRelativeWithIntrinsicBounds(
                if (isSelectGas) R.drawable.icon_checked else R.drawable.icon_uncheck, 0, 0, 0
            )
            updateSendStatus(isClickAll)
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
        sign.type = 1
        sign.decimal = data.decimal
        sign.chainId = UserWalletInfoManager.getUserWalletInfo().userChain.chain_id
        sign.rpc = UserWalletInfoManager.getUserWalletInfo().userChain.rpc.toString()
        sign.fromAddr = chainTokenInfo?.user_wallet_address
        sign.toAddr = binding.etToAddress.text.toString()
        sign.transType = data.trans_type
        sign.contractAddress = chainTokenInfo?.contract_address
        sign.tokenId = ""
        sign.value = binding.etInputNum.text.toString()
        sign.middleContractAddress =
            UserWalletInfoManager.getUserWalletInfo().userChain.middle_contract_address
        Go23WalletManage.getInstance()
            .sign(this, supportFragmentManager, sign, object : MpcUtil.SignCallBack {
                override fun success(response: SignResponse?) {
                    dismissProgress()
                    if (response?.code.toString() == "0") {
                        dismissProgress()
                        sendCoinResultDialog = SendCoinResultDialog(
                            this@SendCoinActivity, true, response?.data ?: ""
                        )
                        sendCoinResultDialog?.show(
                            supportFragmentManager, "sendCoinResultDialog"
                        )
                    } else {
                        CustomToast.showShort(
                            response?.msg ?: getString(R.string.transaction_failed)
                        )
                    }
                }

                override fun failed() {
                    dismissProgress()
                    CustomToast.showShort(R.string.transaction_failed)
                }
            })
    }

    private fun updateSendStatus(isAll: Boolean) {
        val inputStr = binding.etInputNum.text.toString()
        if (inputStr.isNullOrEmpty()) {
            return
        }
        var totalValue = format.parse(inputStr) as? BigDecimal ?: BigDecimal(0)
        var availableNum = BigDecimal(0)
        preTokenSend?.let {
            availableNum = if (chainTokenInfo?.contract_address.isNullOrEmpty()) format.parse(
                it.platform_balance_sort.toString()
            ) as? BigDecimal ?: BigDecimal(0) else format.parse(
                it.token_balance_sort.toString()
            ) as? BigDecimal ?: BigDecimal(0)
        }
//        binding.tvTotalValue.text =
//            "${if (isAll) availableNum else inputStr} ${chainTokenInfo?.symbol}"
        binding.tvSend.isEnabled =
            totalValue > BigDecimal(0) && if (chainTokenInfo?.contract_address.isNullOrEmpty()) {
                totalValue <= (availableNum - BigDecimal(preTokenSend?.gas ?: "0"))
            } else {
                totalValue <= availableNum
            }
                    && (preTokenSend?.trans_type ?: 0) != 0
                    && !TextUtils.isEmpty(binding.etToAddress.text)
                    && isSelectGas
                    && totalValue > (format.parse(preTokenSend?.token_minimum ?: "0") as? BigDecimal
                ?: BigDecimal(0))
//                    && !TextUtils.isEmpty(binding.tvTotalValue.text)
    }
}