package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.coins.app.BaseCallBack
import com.coins.app.BaseResponse
import com.coins.app.Go23WalletInfoManage
import com.coins.app.Go23WalletTokensManage
import com.coins.app.bean.token.TokenListResponse
import com.go23wallet.mpcwalletdemo.adapter.TokenAdapter
import com.go23wallet.mpcwalletdemo.data.UserWalletInfo
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.TokenListLiveData
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.TokenDetailsActivity

class TokenFragment : Fragment() {

    private lateinit var binding: FragmentTabLayoutBinding

    private var mAdapter: TokenAdapter? = null

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
        Go23WalletTokensManage.getInstance().requestUserTokens(
            UserWalletInfoManager.getUserWalletInfo().userWalletId,
            UserWalletInfoManager.getUserWalletInfo().userChainId,
            1, 20,
            object : BaseCallBack<TokenListResponse> {
                override fun success(data: TokenListResponse?) {
                    val list = data?.data?.list
                    TokenListLiveData.setTokenList(list)
                    mAdapter?.setNewInstance(list)
                }

                override fun failed() {
                }
            })
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
                putExtra("id", itemData.token_id)
            })
        }
    }

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()

            val fragment = TokenFragment()
            fragment.arguments = args
            return fragment
        }
    }
}