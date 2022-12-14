package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletTokensManage
import com.coins.app.bean.token.TokenDetail
import com.coins.app.bean.token.TokenDetailResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityTokenDetailsBinding
import com.go23wallet.mpcwalletdemo.dialog.ReceiveDialog
import com.go23wallet.mpcwalletdemo.fragment.TokenTypeFragment
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

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

    private var tokenDetail: TokenDetail? = null
    private var tokenId = 0

    override fun initViews(savedInstanceState: Bundle?) {
        tokenId = intent.getIntExtra("token_id", 0) ?: 0
        initData()
        setListener()
    }

    private fun initData() {
        Go23WalletTokensManage.getInstance()
            .requestTokenDetail(tokenId, object : BaseCallBack<TokenDetailResponse> {
                override fun success(data: TokenDetailResponse?) {
                    data?.data?.let {
                        tokenDetail = it
                        initView()
                        GlideUtils.loadImg(this@TokenDetailsActivity, it.url, binding.ivCoin)
                        GlideUtils.loadImg(this@TokenDetailsActivity, it.url, binding.ivCorner)
                        binding.tvCoinNickname.text = it.name
                        binding.tvCoinName.text = it.name

                        binding.tvNum.text = it.balance
                        binding.tvValue.text = it.balance
                    }
                }

                override fun failed() {
                }
            })

    }

    private fun initView() {
        fragments.add(TokenTypeFragment.newInstance(0))
        fragments.add(TokenTypeFragment.newInstance(1))
        fragments.add(TokenTypeFragment.newInstance(2))
        fragments.add(TokenTypeFragment.newInstance(3))
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
                ReceiveDialog(this, tokenDetail?.name ?: "", tokenDetail?.addr ?: "").apply {
                    show(supportFragmentManager, "receiveDialog")
                }
        }
        binding.tvSend.setOnClickListener {
            startActivity(Intent(this, SendCoinActivity::class.java).apply {
                putExtra("data", "")
            })
        }
    }
}