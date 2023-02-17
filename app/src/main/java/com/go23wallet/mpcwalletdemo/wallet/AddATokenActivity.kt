package com.go23wallet.mpcwalletdemo.wallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.Go23WalletManage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.go23.bean.token.TokenListResponse
import com.go23.bean.token.TokenResponse
import com.go23.callback.BaseCallBack
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.AddTokenListAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddATokenBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.Constant
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.view.LoadMoreListener

class AddATokenActivity : BaseActivity<ActivityAddATokenBinding>() {

    private var mAdapter: AddTokenListAdapter? = null

    override val layoutRes: Int = R.layout.activity_add_a_token

    private var hasChange = false

    private var page = 1

    private var listener: LoadMoreListener? = null

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        initData()
        setListener()
    }

    private fun initData() {
        showProgress()
        Go23WalletManage.getInstance().requestTokens(
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            page, Constant.PAGE_SIZE,
            object : BaseCallBack<TokenListResponse> {
                override fun success(data: TokenListResponse?) {
                    listener?.setIsEnd(data?.data?.list?.isEmpty() ?: true)
                    if (page == 1) {
                        dismissProgress()
                        mAdapter?.setNewInstance(data?.data?.list)
                    } else {
                        data?.data?.list?.let { mAdapter?.addData(it) }
                    }
                }

                override fun failed() {
                    dismissProgress()
                }
            })
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
        listener?.let { binding.recyclerView.removeOnScrollListener(it) }
        listener = object : LoadMoreListener(binding.recyclerView.layoutManager) {
            override fun onLoadMore() {
                page++
                initData()
            }
        }
        binding.recyclerView.addOnScrollListener(listener as LoadMoreListener)

        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = mAdapter?.getItem(position) ?: return
                if (item.contract_address.isNullOrEmpty()) return
                showProgress()
                if (item.isIs_selected) {
                    Go23WalletManage.getInstance().removeToken(
                        item.contract_address,
                        UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                        UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
                        object : BaseCallBack<TokenResponse> {
                            override fun success(data: TokenResponse?) {
                                dismissProgress()
                                item.isIs_selected = !item.isIs_selected
                                hasChange = true
                                mAdapter?.notifyItemChanged(position)
                                CustomToast.showShort(getString(R.string.lite_remove_success))
                            }

                            override fun failed() {
                                dismissProgress()
                            }
                        })
                } else {
                    Go23WalletManage.getInstance().addToken(
                        item.contract_address,
                        UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                        UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
                        object : BaseCallBack<TokenResponse> {
                            override fun success(data: TokenResponse?) {
                                dismissProgress()
                                val token = data?.data ?: return
                                hasChange = true
                                item.isIs_selected = !item.isIs_selected
                                mAdapter?.notifyItemChanged(position)
                                CustomToast.showShort(getString(R.string.lite_add_success))
                            }

                            override fun failed() {
                                dismissProgress()
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
            UpdateDataLiveData.setUpdateType(1)
        }
    }
}