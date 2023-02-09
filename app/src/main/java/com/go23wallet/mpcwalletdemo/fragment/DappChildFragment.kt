package com.go23wallet.mpcwalletdemo.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.DappChildAdapter
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.data.DappItem
import com.go23wallet.mpcwalletdemo.databinding.FragmentDappChildBinding

class DappChildFragment : BaseFragment<FragmentDappChildBinding>() {

    private var url = ""
    private var index = 0

    private var mAdapter: DappChildAdapter? = null

    private val list = mutableListOf(
        DappItem(
            "PancakeSwap",
            "The #1 AMM and yield farm on Binance Smart Chain",
            "https://pancakeswap.finance/swap?utm_source=bitkeep&_needChain=bnb",
            R.drawable.icon_dapp_1,
            R.drawable.icon_chain_1
        ),
        DappItem(
            "Uniswap V3",
            "A protocol for trading and automated liquidity provision on Ethereum",
            "https://app.uniswap.org/#/swap?chain=mainnet&utm_source=bitkeep&_needChain=matic",
            R.drawable.icon_dapp_1,
            R.drawable.icon_chain_2
        )
    )

    override fun initViews() {
        index = arguments?.getInt("index") ?: 0
        if (url.isNullOrEmpty()) return

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            if (mAdapter == null) {
                mAdapter = DappChildAdapter(context)
            }
            adapter = mAdapter
        }
        mAdapter?.setNewInstance(mutableListOf(list[index]))

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            // TODO
        }
    }


    companion object {
        fun newInstance(index: Int, url: String): BaseFragment<out ViewBinding> {
            val args = Bundle()
            args.putString("url", url)
            args.putInt("index", index)
            val fragment = DappChildFragment()
            fragment.arguments = args
            return fragment
        }
    }
}