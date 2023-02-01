package com.go23wallet.mpcwalletdemo.fragment

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.FragmentDappBinding

class DappFragment : BaseFragment<FragmentDappBinding>() {

    override fun initViews() {
    }

    companion object {
        fun newInstance(): BaseFragment<out ViewBinding> {
            return DappFragment()
        }
    }
}