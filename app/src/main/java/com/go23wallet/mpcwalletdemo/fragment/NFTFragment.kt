package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.Go23WalletManage
import com.go23.callback.BaseCallBack
import com.go23.bean.nft.NftListResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.NFTAdapter
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.NFTDetailsActivity

class NFTFragment : BaseFragment<FragmentTabLayoutBinding>() {

    private var mAdapter: NFTAdapter? = null

    private val emptyView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false).apply {
            layoutParams =
                ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
    }

    override fun initViews() {
        initView()
        initData()
    }

    private fun initData() {
        Go23WalletManage.getInstance().requestUserNfts(
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            1, 20,
            object : BaseCallBack<NftListResponse> {
                override fun success(data: NftListResponse?) {
                    mAdapter?.setNewInstance(data?.data?.list)
                }

                override fun failed() {
                }
            })
    }

    private fun initView() {
        UpdateDataLiveData.liveData.observe(viewLifecycleOwner) {
            if (it == 2) {
                initData()
            }
        }
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            if (mAdapter == null) {
                mAdapter = NFTAdapter(context)
            }
            adapter = mAdapter
        }
        mAdapter?.setEmptyView(emptyView)
        mAdapter?.setOnItemClickListener { _, _, position ->
            val itemData = mAdapter?.data?.get(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, NFTDetailsActivity::class.java).apply {
                putExtra("contract_address", itemData.contract_address)
                putExtra("token_id", itemData.token_id)
            })
        }
    }

    override fun updateOffset(offset: Int) {
        val params = emptyView.layoutParams
        params.height = binding.recyclerView.height - offset
        emptyView.layoutParams = params
    }

    override fun onDestroyView() {
        super.onDestroyView()
        UpdateDataLiveData.clearType()
    }

    companion object {
        fun newInstance(): BaseFragment<out ViewBinding> {
            val args = Bundle()

            val fragment = NFTFragment()
            fragment.arguments = args
            return fragment
        }
    }
}