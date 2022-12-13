package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.NFTAttributeAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityNftDetailsBinding
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class NFTDetailsActivity : BaseActivity<ActivityNftDetailsBinding>() {

    private var mAdapter: NFTAttributeAdapter? = null

    override val layoutRes: Int = R.layout.activity_nft_details

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        setListener()
    }

    private fun initView() {
        GlideUtils.loadImg(this, "", binding.ivNft)
        binding.tvNftNo.text = ""
        binding.tvNftName.text = ""
        binding.tvDescriptionContent.text = ""
        binding.tvAddress.text = ""
        binding.tvWebsite.text = ""

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@NFTDetailsActivity, RecyclerView.HORIZONTAL, false)
            if (mAdapter == null) {
                mAdapter = NFTAttributeAdapter()
            }
            adapter = mAdapter
        }
        mAdapter?.setNewInstance(null)
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvTransfer.setOnClickListener {
            startActivity(Intent(this@NFTDetailsActivity, SendNFTActivity::class.java).apply {
                putExtra("id", "")
            })
        }
    }
}