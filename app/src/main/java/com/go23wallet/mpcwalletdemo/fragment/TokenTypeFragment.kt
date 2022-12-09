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
import com.go23wallet.mpcwalletdemo.adapter.TokenTypeAdapter
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.wallet.ChargeDetailsActivity

class TokenTypeFragment : Fragment() {

    private lateinit var binding: FragmentTabLayoutBinding

    private var mAdapter: TokenTypeAdapter? = null

    private var type: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        type = arguments?.getInt("type", 0) ?: 0
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
                mAdapter = TokenTypeAdapter()
            }
            adapter = mAdapter
        }

        mAdapter?.setOnItemClickListener { _, _, position ->
            val item = mAdapter?.getItem(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, ChargeDetailsActivity::class.java).apply {
                putExtra("id", item)
            })
        }
    }

    companion object {
        fun newInstance(type: Int): Fragment {
            val args = Bundle()

            val fragment = TokenFragment()
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }
}