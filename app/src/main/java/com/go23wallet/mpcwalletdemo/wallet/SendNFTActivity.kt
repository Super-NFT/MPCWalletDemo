package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.nft.Nft
import com.coins.app.bean.token.TokenResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendNftBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.google.zxing.activity.CaptureActivity

class SendNFTActivity : BaseActivity<ActivitySendNftBinding>() {

    override val layoutRes: Int = R.layout.activity_send_nft

    private var nftInfo: Nft? = null

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
    }

    private fun setListener() {
        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val data = it.data?.getStringExtra("result") ?: ""
            binding.etAddress.setText(data)
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
                        UserWalletInfoManager.getUserWalletInfo().userChainId,
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

        }
    }
}