package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.NFTAttributeAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityNftDetailsBinding
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendNftBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.google.zxing.activity.CaptureActivity

class SendNFTActivity : BaseActivity<ActivitySendNftBinding>() {

    override val layoutRes: Int = R.layout.activity_send_nft

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        setListener()
    }

    private fun initView() {
        GlideUtils.loadImg(this, "", binding.ivNft)
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
                            registerResult.launch(Intent(this@SendNFTActivity, CaptureActivity::class.java))
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
        binding.tvCancel.setOnClickListener {
            finish()
        }
        binding.tvConfirm.setOnClickListener {

        }
    }
}