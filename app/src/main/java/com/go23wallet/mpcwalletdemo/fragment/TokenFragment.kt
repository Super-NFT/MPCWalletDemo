package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.go23wallet.mpcwalletdemo.adapter.TokenAdapter
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.wallet.TokenDetailsActivity

class TokenFragment : Fragment() {

    private lateinit var binding: FragmentTabLayoutBinding

    private var mAdapter: TokenAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                mAdapter = TokenAdapter(context)
            }
            adapter = mAdapter
        }
        mAdapter?.setNewInstance(mutableListOf("123"))
        mAdapter?.setOnItemClickListener { _, _, position ->
            val itemData = mAdapter?.data?.get(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, TokenDetailsActivity::class.java).apply {
                putExtra("id", itemData)
            })
        }
    }

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()

            val fragment = TokenFragment()
            fragment.arguments = args
            return fragment
        }
    }
}