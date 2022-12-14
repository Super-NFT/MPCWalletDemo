package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.nft.NftListResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.NFTAdapter
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.NFTDetailsActivity

class NFTFragment : Fragment() {

    private lateinit var binding: FragmentTabLayoutBinding

    private var mAdapter: NFTAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        mAdapter?.setEmptyView(R.layout.empty_layout)
        mAdapter?.setOnItemClickListener { _, _, position ->
            val itemData = mAdapter?.data?.get(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, NFTDetailsActivity::class.java).apply {
                putExtra("contract_address", itemData.contract_address)
                putExtra("token_id", itemData.token_id)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        UpdateDataLiveData.clearType()
    }

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()

            val fragment = NFTFragment()
            fragment.arguments = args
            return fragment
        }
    }
}