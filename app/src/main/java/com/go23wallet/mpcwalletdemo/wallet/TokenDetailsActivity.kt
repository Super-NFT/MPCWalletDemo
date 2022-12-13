package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityTokenDetailsBinding
import com.go23wallet.mpcwalletdemo.dialog.ReceiveDialog
import com.go23wallet.mpcwalletdemo.fragment.TokenTypeFragment
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class TokenDetailsActivity : BaseActivity<ActivityTokenDetailsBinding>() {

    private val tabList by lazy {
        mutableListOf(getString(R.string.type_all), getString(R.string.type_out), getString(R.string.type_in), getString(R.string.failed))
    }
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    private var receiveDialog: ReceiveDialog?= null


    override val layoutRes: Int = R.layout.activity_token_details

    override fun initViews(savedInstanceState: Bundle?) {
        initView()
        setListener()
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

        GlideUtils.loadImg(this, "", binding.ivCoin)
        GlideUtils.loadImg(this, "", binding.ivCorner)
        binding.tvCoinNickname.text = ""
        binding.tvCoinName.text = ""

        binding.tvNum.text = ""
        binding.tvValue.text = ""
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvReceive.setOnClickListener {
            receiveDialog = ReceiveDialog(this, "").apply {
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