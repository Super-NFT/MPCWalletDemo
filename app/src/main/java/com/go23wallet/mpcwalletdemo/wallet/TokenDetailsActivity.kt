package com.go23wallet.mpcwalletdemo.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.databinding.ActivityTokenDetailsBinding
import com.go23wallet.mpcwalletdemo.fragment.TokenTypeFragment
import com.go23wallet.mpcwalletdemo.utils.GlideUtils

class TokenDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTokenDetailsBinding
    private val tabList = mutableListOf(getString(R.string.type_all), getString(R.string.type_out), getString(R.string.type_in), getString(R.string.failed))
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokenDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

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

        }
        binding.tvSend.setOnClickListener {

        }
    }
}