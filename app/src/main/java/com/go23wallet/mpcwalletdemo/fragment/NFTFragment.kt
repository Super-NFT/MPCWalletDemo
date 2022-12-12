package com.go23wallet.mpcwalletdemo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.go23wallet.mpcwalletdemo.adapter.NFTAdapter
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.wallet.NFTDetailsActivity

class NFTFragment : Fragment() {

    private lateinit var binding: FragmentTabLayoutBinding

    private var mAdapter: NFTAdapter? = null

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
        UpdateDataLiveData.liveData.observe(viewLifecycleOwner, Observer {
            if (it == 2) {

            }
        })
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            if (mAdapter == null) {
                mAdapter = NFTAdapter(context)
            }
            adapter = mAdapter
        }
        mAdapter?.setNewInstance(mutableListOf("123", "45", "35"))
        mAdapter?.setOnItemClickListener { _, _, position ->
            val itemData = mAdapter?.data?.get(position) ?: return@setOnItemClickListener
            startActivity(Intent(context, NFTDetailsActivity::class.java).apply {
                putExtra("id", itemData)
            })
        }
    }

    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()

            val fragment = NFTFragment()
            fragment.arguments = args
            return fragment
        }
    }
}