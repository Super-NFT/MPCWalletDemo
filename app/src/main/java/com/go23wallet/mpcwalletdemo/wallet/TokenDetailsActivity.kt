package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.token.Token
import com.coins.app.bean.token.TokenDetailResponse
import com.coins.app.bean.token.TokenResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivityTokenDetailsBinding
import com.go23wallet.mpcwalletdemo.dialog.ReceiveDialog
import com.go23wallet.mpcwalletdemo.fragment.TokenTransactionsFragment
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class TokenDetailsActivity : BaseActivity<ActivityTokenDetailsBinding>() {

    private val tabList by lazy {
        mutableListOf(
            getString(R.string.type_all),
            getString(R.string.type_out),
            getString(R.string.type_in),
            getString(R.string.failed)
        )
    }
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    private var receiveDialog: ReceiveDialog? = null

    override val layoutRes: Int = R.layout.activity_token_details

    private var token: Token? = null

    override fun initViews(savedInstanceState: Bundle?) {
        token = intent.getSerializableExtra("data") as Token?
        if (token == null) {
            finish()
            return
        }

        initData()
        setListener()
    }

    private fun initData() {
        token?.let {
            GlideUtils.loadImg(this@TokenDetailsActivity, it.image_url, binding.ivCoin)
            GlideUtils.loadImg(
                this@TokenDetailsActivity,
                it.chain_image_url,
                binding.ivCorner
            )
            binding.tvCoinNickname.text = it.symbol
            binding.tvCoinName.text = UserWalletInfoManager.getUserWalletInfo().userChain.name

            binding.tvBalance.text = it.balance
            binding.tvValue.visibility =
                if (it.balance_u.toDouble() > 0) View.VISIBLE else View.INVISIBLE
            binding.tvValue.text = "$${it.balance_u}"
            initView()
        }
    }

    private fun refreshData() {
        Go23WalletManage.getInstance().requestTokenDetail(
            token?.chain_id ?: 0,
            token?.contract_address ?: "",
            object : BaseCallBack<TokenResponse> {
                override fun success(data: TokenResponse?) {
                    data?.let { response ->
                        if (response.code == 0) {
                            response.data?.let {
                                binding.tvValue.visibility =
                                    if (it.balance_u.toDouble() > 0) View.VISIBLE else View.INVISIBLE
                                binding.tvBalance.text = it.balance
                                binding.tvValue.text = "$${it.balance_u}"
                                tabAdapter?.notifyDataSetChanged()
                            }
                        } else {
                            CustomToast.showShort(response.message)
                        }
                    }
                }

                override fun failed() {
                }
            })
    }

    private fun initView() {
        if (fragments.size > 0) return
        fragments.add(
            TokenTransactionsFragment.newInstance(
                tabList[0].lowercase(Locale.ROOT),
                token?.contract_address ?: ""
            )
        )
        fragments.add(
            TokenTransactionsFragment.newInstance(
                tabList[1].lowercase(Locale.ROOT),
                token?.contract_address ?: ""
            )
        )
        fragments.add(
            TokenTransactionsFragment.newInstance(
                tabList[2].lowercase(Locale.ROOT),
                token?.contract_address ?: ""
            )
        )
        fragments.add(
            TokenTransactionsFragment.newInstance(
                "fail",
                token?.contract_address ?: ""
            )
        )
        tabAdapter = TabFragmentAdapter(supportFragmentManager).apply {
            setList(fragments, tabList)
        }
        binding.viewPager.adapter = tabAdapter
        binding.tab.setupWithViewPager(binding.viewPager)
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.refreshView.setOnRefreshListener {
            refreshData()
            lifecycleScope.launch {
                delay(1000)
                runOnUiThread {
                    binding.refreshView.isRefreshing = false
                }
            }
        }
        binding.tvReceive.setOnClickListener {
            receiveDialog =
                ReceiveDialog(this, token?.name ?: "", token?.user_wallet_address ?: "").apply {
                    show(supportFragmentManager, "receiveDialog")
                }
        }
        binding.tvSend.setOnClickListener {
            token?.let { data ->
                startActivity(Intent(this, SendCoinActivity::class.java).apply {
//                    putExtra("token_id", data.token_id)
                    putExtra(
                        "data",
                        ChainTokenInfo(
                            data.chain_id,
                            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                            data.name,
                            data.symbol,
                            data.image_url,
                            data.contract_address
                        )
                    )
                })
            }

        }
    }
}