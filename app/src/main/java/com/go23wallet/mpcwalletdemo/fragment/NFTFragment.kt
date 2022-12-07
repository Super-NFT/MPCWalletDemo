package com.go23wallet.mpcwalletdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.go23wallet.mpcwalletdemo.adapter.NFTAdapter
import com.go23wallet.mpcwalletdemo.databinding.FragmentTabLayoutBinding

class NFTFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            if (mAdapter == null) {
                mAdapter = NFTAdapter(context)
            }
            adapter = mAdapter
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