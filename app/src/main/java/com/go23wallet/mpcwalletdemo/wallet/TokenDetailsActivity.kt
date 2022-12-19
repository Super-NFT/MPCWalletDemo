package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.coins.app.bean.token.Token
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivityTokenDetailsBinding
import com.go23wallet.mpcwalletdemo.dialog.ReceiveDialog
import com.go23wallet.mpcwalletdemo.fragment.TokenTransactionsFragment
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
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
    private var tokenId = 0

    override fun initViews(savedInstanceState: Bundle?) {
        tokenId = intent.getIntExtra("token_id", 0) ?: 0
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
            binding.tvCoinName.text = it.name

            binding.tvBalance.text = it.balance
            binding.tvValue.text = "$${it.balance_u}"
            initView()
        }

    }

    private fun initView() {
        fragments.add(
            TokenTransactionsFragment.newInstance(
                tabList[0].lowercase(Locale.ROOT),
                token?.addr ?: ""
            )
        )
        fragments.add(
            TokenTransactionsFragment.newInstance(
                tabList[1].lowercase(Locale.ROOT),
                token?.addr ?: ""
            )
        )
        fragments.add(
            TokenTransactionsFragment.newInstance(
                tabList[2].lowercase(Locale.ROOT),
                token?.addr ?: ""
            )
        )
        fragments.add(
            TokenTransactionsFragment.newInstance(
                tabList[3].lowercase(Locale.ROOT),
                token?.addr ?: ""
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
        binding.tvReceive.setOnClickListener {
            receiveDialog =
                ReceiveDialog(this, token?.name ?: "", token?.addr ?: "").apply {
                    show(supportFragmentManager, "receiveDialog")
                }
        }
        binding.tvSend.setOnClickListener {
            token?.let { data ->
                startActivity(Intent(this, SendCoinActivity::class.java).apply {
                    putExtra("token_id", data.token_id)
                    putExtra(
                        "data",
                        ChainTokenInfo(
                            data.block_chain_id,
                            UserWalletInfoManager.getUserWalletInfo().walletAddress,
                            data.name,
                            data.symbol,
                            data.image_url,
                            data.addr
                        )
                    )
                })
            }

        }
    }
}