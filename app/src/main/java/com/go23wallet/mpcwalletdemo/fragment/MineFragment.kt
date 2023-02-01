package com.go23wallet.mpcwalletdemo.fragment

import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.SPUtils
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.FragmentMineBinding

class MineFragment : BaseFragment<FragmentMineBinding>() {

    override fun initViews() {
        binding.tvEmail.text = SPUtils.getInstance().getString("email")
    }

    companion object {
        fun newInstance(): BaseFragment<out ViewBinding> {
            return MineFragment()
        }
    }
}