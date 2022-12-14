package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletTokensManage
import com.coins.app.bean.token.Token
import com.coins.app.bean.token.TokenListResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.AddTokenListAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityAddATokenBinding
import com.go23wallet.mpcwalletdemo.livedata.TokenListLiveData
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddATokenActivity : BaseActivity<ActivityAddATokenBinding>() {

    private var mAdapter: AddTokenListAdapter? = null

    private var selectList = mutableListOf<Token>()

    override val layoutRes: Int = R.layout.activity_add_a_token

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        initData()
        setListener()
    }

    private fun initData() {
        TokenListLiveData.liveData.observe(this, Observer {
            if (it != null) {
                selectList = it
                Go23WalletTokensManage.getInstance().requestUserTokens(
                    UserWalletInfoManager.getUserWalletInfo().userWalletId,
                    UserWalletInfoManager.getUserWalletInfo().userChainId,
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
        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = mAdapter?.getItem(position) ?: return
                Go23WalletTokensManage.getInstance().requestUserTokens(
                    UserWalletInfoManager.getUserWalletInfo().userWalletId,
                    UserWalletInfoManager.getUserWalletInfo().userChainId,
                    1, 20,
                    object : BaseCallBack<TokenListResponse> {
                        override fun success(data: TokenListResponse?) {
                            val list = data?.data?.list
                            selectList.add(item)
                            mAdapter?.notifyItemChanged(position)
                        }

                        override fun failed() {
                        }
                    })
            }
        })
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvCustom.setOnClickListener {
            startActivity(Intent(this@AddATokenActivity, AddCustomTokenActivity::class.java))
        }
    }

    override fun finish() {
        super.finish()
        UpdateDataLiveData.liveData.postValue(1)
    }
}