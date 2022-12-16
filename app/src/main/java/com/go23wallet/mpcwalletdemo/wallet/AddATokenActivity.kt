package com.go23wallet.mpcwalletdemo.wallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.token.Token
import com.coins.app.bean.token.TokenListResponse
import com.coins.app.bean.token.TokenResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.AddTokenListAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddATokenBinding
import com.go23wallet.mpcwalletdemo.livedata.TokenListLiveData
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager

class AddATokenActivity : BaseActivity<ActivityAddATokenBinding>() {

    private var mAdapter: AddTokenListAdapter? = null

    private var selectList = mutableListOf<Token>()

    override val layoutRes: Int = R.layout.activity_add_a_token

    private var hasChange = false

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        initData()
        setListener()
    }

    private fun initData() {
        TokenListLiveData.liveData.observe(this) {
            if (it != null) {
                selectList = it
                Go23WalletManage.getInstance().requestTokens(
                    UserWalletInfoManager.getUserWalletInfo().userChain.block_chain_id,
                    1, 20,
                    object : BaseCallBack<TokenListResponse> {
                        override fun success(data: TokenListResponse?) {
                            val list = data?.data?.list
                            mAdapter?.let { adapter ->
                                adapter.setTokenList(selectList)
                                adapter.setNewInstance(list)
                            }
                        }

                        override fun failed() {
                        }
                    })
            }
        }
    }

    private fun initView() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@AddATokenActivity)
            if (mAdapter == null) {
                mAdapter = AddTokenListAdapter(this@AddATokenActivity)
            }
            adapter = mAdapter
        }
    }

    private fun setListener() {
        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = mAdapter?.getItem(position) ?: return
                val index = selectList.indexOfFirst { item.token_id == it.token_id }
                if (index < 0) {
                    Go23WalletManage.getInstance().addToken(
                        item.token_id,
                        UserWalletInfoManager.getUserWalletInfo().userChain.block_chain_id,
                        UserWalletInfoManager.getUserWalletInfo().walletId,
                        object : BaseCallBack<TokenResponse> {
                            override fun success(data: TokenResponse?) {
                                val token = data?.data ?: return
                                hasChange = true
                                selectList.add(item)
                                mAdapter?.notifyItemChanged(position)
                            }

                            override fun failed() {
                            }
                        })
                } else {
                    Go23WalletManage.getInstance().removeToken(
                        item.token_id,
                        UserWalletInfoManager.getUserWalletInfo().userChain.block_chain_id,
                        UserWalletInfoManager.getUserWalletInfo().walletId,
                        object : BaseCallBack<TokenResponse> {
                            override fun success(data: TokenResponse?) {
                                hasChange = true
                                selectList.removeAt(index)
                                mAdapter?.notifyItemChanged(position)
                            }

                            override fun failed() {
                            }
                        })
                }
            }
        })
        binding.ivBack.setOnClickListener {
            finish()
        }

        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                finish()
            }
        }

        binding.tvCustom.setOnClickListener {
            registerResult.launch(
                Intent(
                    this@AddATokenActivity,
                    AddCustomTokenActivity::class.java
                )
            )
        }
    }

    override fun finish() {
        super.finish()
        if (hasChange) {
            UpdateDataLiveData.liveData.postValue(1)
        }
    }
}