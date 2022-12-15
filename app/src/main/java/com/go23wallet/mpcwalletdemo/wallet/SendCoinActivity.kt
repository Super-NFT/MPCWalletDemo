package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.coins.app.BaseCallBack
import com.coins.app.bean.chain.UserChain
import com.coins.app.bean.transaction.PreTokenSend
import com.coins.app.bean.transaction.PreTokenSendResponse
import com.coins.app.manage.Go23WalletTransactionManage
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendCoinBinding
import com.go23wallet.mpcwalletdemo.dialog.SelectTokenSendDialog
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.google.zxing.activity.CaptureActivity

class SendCoinActivity : BaseActivity<ActivitySendCoinBinding>() {

    private val selectTokenSendDialog: SelectTokenSendDialog by lazy {
        SelectTokenSendDialog(this)
    }

    override val layoutRes: Int = R.layout.activity_send_coin

    private var tokenId = 0
    private var chainTokenInfo: ChainTokenInfo? = null
    private var preTokenSend: PreTokenSend? = null

    override fun initViews(savedInstanceState: Bundle?) {
        tokenId = intent.getIntExtra("token_id", 0)
        chainTokenInfo = intent.getParcelableExtra("data")
        chainTokenInfo?.let {
            GlideUtils.loadImg(this, it.imgUrl, binding.ivCoinIcon)
            binding.tvCoinName.text = it.name
            binding.tvFromCoinNickname.text = it.symbol
        } ?: kotlin.run {
            finish()
            return
        }

        initData()
        setListener()
    }

    private fun initData() {
        chainTokenInfo?.let {
            Go23WalletTransactionManage.getInstance().requestPreTokenSend(
                tokenId,
                it.blockChainId,
                it.addr,
                object : BaseCallBack<PreTokenSendResponse> {
                    override fun success(data: PreTokenSendResponse?) {
                        data?.data?.let { preToken ->
                            preTokenSend = preToken
                            binding.etInputNum.setText("0.00")
                            binding.etToAddress.setText("")
                            binding.tvGasTips.visibility =
                                if (preToken.isIs_lending_gas) View.VISIBLE else View.GONE
                            binding.tvFromAddress.text = it.addr
                            binding.tvAvailable.text =
                                String.format(
                                    getString(R.string.available),
                                    "${if (tokenId == 0) preToken.platform_balance else preToken.token_balance} ${it.symbol}"
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
            tokenId = it.token_id
            chainTokenInfo =
                ChainTokenInfo(it.block_chain_id, it.addr, it.name, it.symbol, it.image_url)
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
                    return
                }
                val num = s.toString().toDouble()

                val uPer = if (tokenId == 0) {
                    preTokenSend?.platform_u_per
                } else {
                    preTokenSend?.token_u_per
                }
                binding.tvInputValue.text = if (uPer == null || uPer <= 0) "" else "=$${num * uPer}"

                var totalValue = 1.0
                var availableNum = 0.0
                preTokenSend?.let {
                    totalValue = num * (1 + it.fee)
                    availableNum = if (tokenId == 0) it.platform_balance else it.token_balance
                }
                binding.tvTotalValue.text = "$totalValue ${chainTokenInfo?.symbol}"
                binding.tvSend.isEnabled = totalValue <= availableNum
            }
        })
        binding.tvPaste.setOnClickListener {
            CopyUtils.pasteText(this, binding.etToAddress)
        }
        binding.tvAll.setOnClickListener {
            preTokenSend?.let {
                val availableNum = if (tokenId == 0) it.platform_balance else it.token_balance
                val num = availableNum * (1 - it.fee)
                binding.etInputNum.setText("$num ${chainTokenInfo?.symbol}")
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
        binding.tvSend.setOnClickListener {

        }
    }
}