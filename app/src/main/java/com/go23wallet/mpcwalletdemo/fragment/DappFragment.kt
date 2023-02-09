package com.go23wallet.mpcwalletdemo.fragment

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.FragmentDappBinding

class DappFragment : BaseFragment<FragmentDappBinding>() {

    private val tabList by lazy {
        mutableListOf("BNB Chain", "Ethereum")
    }

    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    override fun initViews() {
        initView()
    }

    private fun initView() {
        if (fragments.size > 0) return
        fragments.add(DappChildFragment.newInstance(0))
        fragments.add(DappChildFragment.newInstance(1))
        tabAdapter = TabFragmentAdapter(childFragmentManager).apply {
            setList(fragments, tabList)
        }
        binding.viewPager.adapter = tabAdapter
        binding.tab.setupWithViewPager(binding.viewPager)
    }

    companion object {

        fun newInstance(): BaseFragment<out ViewBinding> {
            return DappFragment()
        }
    }
}