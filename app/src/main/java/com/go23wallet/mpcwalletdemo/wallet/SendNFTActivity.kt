package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.Sign
import com.coins.app.bean.nft.Nft
import com.coins.app.bean.token.TokenResponse
import com.coins.app.bean.transaction.PreNFTSendResponse
import com.coins.app.entity.mpc.SignResponse
import com.coins.app.util.MpcUtil
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendNftBinding
import com.go23wallet.mpcwalletdemo.dialog.SendCoinResultDialog
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.google.gson.Gson
import com.google.zxing.activity.CaptureActivity

class SendNFTActivity : BaseActivity<ActivitySendNftBinding>() {

    override val layoutRes: Int = R.layout.activity_send_nft

    private var nftInfo: Nft? = null

    private var sendCoinResultDialog: SendCoinResultDialog? = null

    override fun initViews(savedInstanceState: Bundle?) {
        nftInfo = intent.getSerializableExtra("data") as Nft?
        if (nftInfo == null) {
            finish()
            return
        }
        initView()
        setListener()
    }

    private fun initView() {
        GlideUtils.loadImg(this, nftInfo?.image, binding.ivNft)
        showProgress()
        Go23WalletManage.getInstance().requestPreNFTSend(
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            object : BaseCallBack<PreNFTSendResponse> {
                override fun success(data: PreNFTSendResponse?) {
                    dismissProgress()
                    data?.data?.let {
                        binding.tvConfirm.isEnabled = it.isIs_ok
                        binding.tvGasNum.text = "${it.gas} ${UserWalletInfoManager.getUserWalletInfo().userChain.symbol}"
                        binding.tvGasFailTip.visibility =
                            if (it.isIs_ok) View.GONE else View.VISIBLE
                    } ?: kotlin.run {
                        binding.tvConfirm.isEnabled = false
                    }
                }

                override fun failed() {
                    dismissProgress()
                }
            }
        )
    }

    private fun setListener() {
        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val data = it.data?.getStringExtra("result") ?: ""
            val content = if (data.contains(":")) {
                data.split(":")[1]
            } else {
                data
            }
            binding.etAddress.setText(content)
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivScanCode.setOnClickListener {
            PermissionUtils.permission(PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        try {
                            registerResult.launch(
                                Intent(
                                    this@SendNFTActivity,
                                    CaptureActivity::class.java
                                )
                            )
                        } catch (ignored: Exception) {
                            Toast.makeText(
                                this@SendNFTActivity,
                                getString(R.string.request_camera_permission_fail),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onDenied() {
                        Toast.makeText(
                            this@SendNFTActivity,
                            getString(R.string.request_camera_permission_fail),
                            Toast.LENGTH_SHORT
                        ).show();

                    }
                }).request();
        }
        binding.tvPaste.setOnClickListener {
            CopyUtils.pasteText(this, binding.etAddress)
        }
        binding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val addr = s.toString()
                if (addr.length <= 30) {
                    binding.tvConfirm.isEnabled = false
                } else {
                    Go23WalletManage.getInstance().checkToken(
                        addr,
                        UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
                        object : BaseCallBack<TokenResponse> {
                            override fun success(p0: TokenResponse?) {
                                binding.tvConfirm.isEnabled = true
                            }

                            override fun failed() {
                                binding.tvConfirm.isEnabled = false
                            }
                        })
                }
            }

        })
        binding.tvCancel.setOnClickListener {
            finish()
        }
        binding.tvConfirm.setOnClickListener {
            nftInfo?.let { it1 -> toSign(it1) }
        }
    }

    private fun toSign(nft: Nft) {
        showProgress()
        val sign = Sign()
        val key1 = Go23WalletManage.getInstance()
            .getLocalMpcKey(Go23WalletManage.getInstance().walletAddress)
        sign.type = 1
        sign.chainId = UserWalletInfoManager.getUserWalletInfo().userChain.chain_id
        sign.rpc = UserWalletInfoManager.getUserWalletInfo().userChain.rpc
        sign.fromAddr = UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address
        sign.toAddr = binding.etAddress.text.toString()
        sign.transType = 3
        sign.contractAddress = nft.contract_address
        sign.tokenId = nft.token_id.toString()
        sign.value = ""
        sign.nft_name = nft.name
        sign.middleContractAddress = ""
        Go23WalletManage.getInstance().sign(
            this, supportFragmentManager, key1, sign, object : MpcUtil.SignCallBack {
                override fun success(response: SignResponse?) {
                    dismissProgress()
                    if (response?.code.toString() == "0") {
                        sendCoinResultDialog =
                            SendCoinResultDialog(this@SendNFTActivity, true, response?.data ?: "")
                        sendCoinResultDialog?.show(supportFragmentManager, "sendCoinResultDialog")
                    } else {
                        Toast.makeText(baseContext, "Transaction failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun failed() {
                    dismissProgress()
                    Toast.makeText(baseContext, "Transaction failed", Toast.LENGTH_SHORT).show()
                }
            }
        )

    }
}