package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.Go23WalletManage
import com.go23.bean.transaction.TransactionResponse
import com.go23.callback.BaseCallBack
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TokenTransactionsAdapter
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.ChargeDetailsActivity

class TokenTransactionsFragment : BaseFragment<FragmentTabLayoutBinding>() {


    private var mAdapter: TokenTransactionsAdapter? = null

    private val emptyView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false).apply {
            layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private var transactionType: String = "all"
    private var contractAddress: String = ""

    override fun initViews() {
        transactionType = arguments?.getString("transactionType", "all") ?: "all"
        contractAddress = arguments?.getString("contract_address", "") ?: ""
        initView()
    }

    private fun initView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            if (mAdapter == null) {
                mAdapter = TokenTransactionsAdapter()
            }
            adapter = mAdapter
        }
        emptyView.findViewById<AppCompatTextView>(R.id.tv_tips).text =
            getString(R.string.empty_transaction_tips)
        mAdapter?.setEmptyView(emptyView)

        mAdapter?.setOnItemClickListener { _, _, position ->
            val item = mAdapter?.getItem(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, ChargeDetailsActivity::class.java).apply {
                putExtra("hash", item.hash)
            })
        }

        Go23WalletManage.getInstance().requestTransactionRecords(
            transactionType,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            contractAddress,
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            1,
            20,
            object : BaseCallBack<TransactionResponse> {
                override fun success(data: TransactionResponse?) {
                    mAdapter?.setNewInstance(data?.data?.list)
                }

                override fun failed() {
                }

            })
    }

    override fun updateOffset(offset: Int) {
        val params = emptyView.layoutParams
        params.height = binding.recyclerView.height - offset
        emptyView.layoutParams = params
    }

    companion object {
        fun newInstance(
            transactionType: String,
            contractAddress: String
        ): BaseFragment<out ViewBinding> {
            val args = Bundle()

            val fragment = TokenTransactionsFragment()
            args.putString("transactionType", transactionType)
            args.putString("contract_address", contractAddress)
            fragment.arguments = args
            return fragment
        }
    }
}