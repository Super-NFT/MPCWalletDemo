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
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendCoinBinding
import com.go23wallet.mpcwalletdemo.dialog.SelectTokenSendDialog
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.google.zxing.activity.CaptureActivity

class SendCoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendCoinBinding

    private val selectTokenSendDialog: SelectTokenSendDialog by lazy {
        SelectTokenSendDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendCoinBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {
        GlideUtils.loadImg(this, "", binding.ivCoinIcon)
        binding.tvCoinName.text = ""
        binding.tvCoinType.text = ""
        binding.tvAvailable.text = String.format(getString(R.string.available), "")
        binding.tvFromCoinNickname.text = ""
        binding.tvFromAddress.text = ""
        binding.tvGasTips.visibility = View.VISIBLE

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
            GlideUtils.loadImg(this, "", binding.ivCoinIcon)
            binding.tvCoinName.text = ""
            binding.tvCoinType.text = ""
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
                    binding.tvInputValue.visibility = View.GONE
                    return
                }
                val num = s.toString().toInt()
                binding.tvInputValue.visibility = View.VISIBLE
                binding.tvInputValue.text = ""
                binding.tvTotalValue.text = ""
            }
        })
        binding.tvPaste.setOnClickListener {
            CopyUtils.pasteText(this, binding.etToAddress)
        }
        binding.tvAll.setOnClickListener {

        }
        binding.ivClear.setOnClickListener {
            binding.etInputNum.setText("")
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