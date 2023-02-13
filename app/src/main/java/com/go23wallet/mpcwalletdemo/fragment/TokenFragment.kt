package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.Go23WalletManage
import com.go23.callback.BaseCallBack
import com.go23.bean.token.TokenListResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TokenAdapter
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.TokenDetailsActivity

class TokenFragment : BaseFragment<FragmentTabLayoutBinding>() {

    private var mAdapter: TokenAdapter? = null

    override fun initViews() {
        initView()
        initData()
    }

    private fun initData() {
        Go23WalletManage.getInstance().requestUserTokens(
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            1, 20,
            object : BaseCallBack<TokenListResponse> {
                override fun success(data: TokenListResponse?) {
                    val list = data?.data?.list
                    mAdapter?.setNewInstance(list)
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