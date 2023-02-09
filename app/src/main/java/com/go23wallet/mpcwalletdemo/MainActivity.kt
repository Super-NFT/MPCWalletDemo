package com.go23wallet.mpcwalletdemo

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.databinding.ActivityMainBinding
import com.go23wallet.mpcwalletdemo.fragment.DappFragment
import com.go23wallet.mpcwalletdemo.fragment.MineFragment
import com.go23wallet.mpcwalletdemo.fragment.WalletFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutRes: Int = R.layout.activity_main

    private var lastIndex = 0

    private val walletFragment: BaseFragment<out ViewBinding> by lazy {
        WalletFragment.newInstance(this)
    }

    private val dappFragment: BaseFragment<out ViewBinding> by lazy {
        DappFragment.newInstance()
    }

    private val mineFragment: BaseFragment<out ViewBinding> by lazy {
        MineFragment.newInstance()
    }

    private val fragments = mutableListOf(walletFragment, dappFragment, mineFragment)

    override fun initViews(savedInstanceState: Bundle?) {
        binding.navView.itemIconTintList = null
        supportFragmentManager.beginTransaction().replace(
            R.id.main_pager, fragments[0]
        ).commitAllowingStateLoss()
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_wallet -> {
                    switchFragment(0)
                }
                R.id.navigation_dapp -> {
                    switchFragment(1)
                }
                R.id.navigation_mine -> {
                    switchFragment(2)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun switchFragment(index: Int) {
        if (index >= fragments.size || lastIndex >= fragments.size || index == lastIndex) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
        val lastFragment = fragments[lastIndex]
        if (lastFragment != null) {
            transaction.hide(lastFragment)
        }
        val fragment = fragments[index]
        if (!fragment.isAdded) {
            try {
                transaction.add(R.id.main_pager, fragments[index])
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        window.statusBarColor = getColor(
            if (index == 0 || index == 1) {
                R.color.color_FAFAFA
            } else {
                R.color.white
            }
        )
        lastIndex = index
        transaction.show(fragment)
        transaction.commitAllowingStateLoss()
    }
}