package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.go23wallet.mpcwalletdemo.adapter.NFTAttributeAdapter
import com.go23wallet.mpcwalletdemo.databinding.ActivityNftDetailsBinding
import com.go23wallet.mpcwalletdemo.databinding.ActivitySendNftBinding
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class SendNFTActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendNftBinding

    private var mAdapter: NFTAttributeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendNftBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {
        GlideUtils.loadImg(this, "", binding.ivNft)
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivScanCode.setOnClickListener {

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