package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.nft.Nft
import com.coins.app.bean.nft.NftResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.NFTAttributeAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityNftDetailsBinding
import com.go23wallet.mpcwalletdemo.ext.parseAddress
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager

class NFTDetailsActivity : BaseActivity<ActivityNftDetailsBinding>() {

    private var mAdapter: NFTAttributeAdapter? = null

    override val layoutRes: Int = R.layout.activity_nft_details

    private var contractAddress = ""
    private var tokenId = ""

    private var nft: Nft? = null

    override fun initViews(savedInstanceState: Bundle?) {
        contractAddress = intent.getStringExtra("contract_address") ?: ""
        tokenId = intent.getStringExtra("token_id") ?: ""
        initView()
        initData()
        setListener()
    }

    private fun initData() {
        Go23WalletManage.getInstance().requestNftDetail(contractAddress,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            tokenId,
            object : BaseCallBack<NftResponse> {
                override fun success(data: NftResponse?) {
                    data?.data?.let {
                        nft = it
                        GlideUtils.loadImg(this@NFTDetailsActivity, it.image, binding.ivNft)
                        binding.tvNftName.text = it.name
                        binding.tvSeriesName.text = it.series
                        binding.tvDescriptionContent.text =
                            if (it.description.isNullOrEmpty()) "none" else it.description
                        if (it.attributes.isNullOrEmpty()) {
                            binding.tvAttributes.visibility = View.GONE
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            mAdapter?.setNewInstance(it.attributes)
                        }
                        binding.tvAddress.text = it.contract_address.parseAddress()
                        binding.tvTokenAddress.text = it.token_id.toString().parseAddress()
                        binding.tvWebsiteAddress.text =
                            if (it.external_url.isNullOrEmpty()) "none" else it.external_url
                        binding.tvBlockchainAddress.text = it.block_chain_name.toString()
                    }
                }

                override fun failed() {
                }

            })

    }

    private fun initView() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@NFTDetailsActivity, RecyclerView.HORIZONTAL, false)
            if (mAdapter == null) {
                mAdapter = NFTAttributeAdapter()
            }
            adapter = mAdapter
        }
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvAddress.setOnClickListener {
            CopyUtils.copyText(this, nft?.contract_address ?: "")
        }
        binding.tvTokenAddress.setOnClickListener {
            CopyUtils.copyText(this, nft?.token_id?.toString() ?: "")
        }
        binding.tvTransfer.setOnClickListener {
            startActivity(Intent(this@NFTDetailsActivity, SendNFTActivity::class.java).apply {
                putExtra("data", nft)
            })
        }
    }
}