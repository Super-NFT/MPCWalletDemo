package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.transaction.TransactionResponse
import com.go23wallet.mpcwalletdemo.adapter.TokenTransactionsAdapter
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.ChargeDetailsActivity

class TokenTransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTabLayoutBinding

    private var mAdapter: TokenTransactionsAdapter? = null

    private var transactionType: String = "all"
    private var contract: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        transactionType = arguments?.getString("transactionType", "all") ?: "all"
        contract = arguments?.getString("contract", "") ?: ""
        binding = FragmentTabLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        mAdapter?.setOnItemClickListener { _, _, position ->
            val item = mAdapter?.getItem(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, ChargeDetailsActivity::class.java).apply {
                putExtra("transaction_id", item.transaction_id)
            })
        }

        Go23WalletManage.getInstance().requestTransactionRecords(
            transactionType,
            UserWalletInfoManager.getUserWalletInfo().userChain.block_chain_id,
            UserWalletInfoManager.getUserWalletInfo().walletAddress,
            contract,
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

    companion object {
        fun newInstance(transactionType: String, contract: String): Fragment {
            val args = Bundle()

            val fragment = TokenTransactionsFragment()
            args.putString("transactionType", transactionType)
            args.putString("contract", contract)
            fragment.arguments = args
            return fragment
        }
    }
}