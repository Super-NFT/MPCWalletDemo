package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.Go23WalletManage
import com.go23.callback.BaseCallBack
import com.go23.bean.token.TokenListResponse
import com.go23wallet.mpcwalletdemo.adapter.TokenAdapter
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.Constant
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.view.LoadMoreListener
import com.go23wallet.mpcwalletdemo.wallet.TokenDetailsActivity

class TokenFragment : BaseFragment<FragmentTabLayoutBinding>() {

    private var mAdapter: TokenAdapter? = null

    private var page = 1

    private var listener: LoadMoreListener? = null

    override fun initViews() {
        page = 1
        initView()
        initData()
    }

    private fun initData() {
        Go23WalletManage.getInstance().requestUserTokens(
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            page,
            Constant.PAGE_SIZE,
            object : BaseCallBack<TokenListResponse> {
                override fun success(data: TokenListResponse?) {
                    listener?.setIsEnd(data?.data?.list?.isEmpty() ?: true)
                    if (page == 1) {
                        mAdapter?.setNewInstance(data?.data?.list)
                    } else {
                        data?.data?.list?.let { mAdapter?.addData(it) }
                    }
                }

                override fun failed() {
                }
            })
    }

    fun showOrHideBalance(isShowBalance: Boolean) {
        mAdapter?.setIsShowBalance(isShowBalance)
    }

    private fun initView() {
        UpdateDataLiveData.liveData.observe(viewLifecycleOwner) {
            if (it == 1) {
                initData()
            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            if (mAdapter == null) {
                mAdapter = TokenAdapter(context)
            }
            adapter = mAdapter
        }
        mAdapter?.setOnItemClickListener { _, _, position ->
            val itemData = mAdapter?.data?.get(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, TokenDetailsActivity::class.java).apply {
                putExtra("data", itemData)
            })
        }
        listener?.let { binding.recyclerView.removeOnScrollListener(it) }
        listener = object : LoadMoreListener(binding.recyclerView.layoutManager) {
            override fun onLoadMore() {
                page++
                initData()
            }
        }
        binding.recyclerView.addOnScrollListener(listener as LoadMoreListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        UpdateDataLiveData.clearType()
    }

    companion object {
        fun newInstance(): BaseFragment<out ViewBinding> {
            val args = Bundle()

            val fragment = TokenFragment()
            fragment.arguments = args
            return fragment
        }
    }


}