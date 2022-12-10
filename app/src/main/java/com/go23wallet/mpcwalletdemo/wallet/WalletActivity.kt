package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.databinding.ActivityWalletBinding
import com.go23wallet.mpcwalletdemo.dialog.ChooseMainnetDialog
import com.go23wallet.mpcwalletdemo.dialog.ImportAssetDialog
import com.go23wallet.mpcwalletdemo.dialog.ReceiveDialog
import com.go23wallet.mpcwalletdemo.dialog.SettingDialog
import com.go23wallet.mpcwalletdemo.fragment.NFTFragment
import com.go23wallet.mpcwalletdemo.fragment.TokenFragment
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.ScreenUtils


class WalletActivity : AppCompatActivity() {

    private val tabList = mutableListOf<String>()
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null
    private lateinit var binding: ActivityWalletBinding

    private val chooseMainnetDialog: ChooseMainnetDialog by lazy {
        ChooseMainnetDialog(this)
    }

    private val settingDialog by lazy {
        SettingDialog(this)
    }

    private val importAssetDialog by lazy {
        ImportAssetDialog(this)
    }

    private var receiveDialog: ReceiveDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
        setListener()
    }

    private fun initView() {
        tabList.add(getString(R.string.tokens))
        tabList.add(getString(R.string.nfts))
        fragments.add(TokenFragment.newInstance())
        fragments.add(NFTFragment.newInstance())
        tabAdapter = TabFragmentAdapter(supportFragmentManager).apply {
            setList(fragments, tabList)
        }
        binding.viewPager.adapter = tabAdapter
        binding.tab.setupWithViewPager(binding.viewPager)
    }

    private fun setListener() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.icMore.setOnClickListener {
            settingDialog.setHeight((ScreenUtils.getScreenHeight(this) * 0.8).toInt())
            settingDialog.show(supportFragmentManager, "settingDialog")
        }

        binding.tvCoinType.setOnClickListener {
            chooseMainnetDialog.apply {
                setSelectId("")
                show(supportFragmentManager, "chooseMainnetDialog")
            }
        }

        chooseMainnetDialog.callback = {
            GlideUtils.loadImg(this, it, binding.ivCoinType)
            binding.tvCoinType.text = it
        }

        binding.tvAddress.setOnClickListener {
            CopyUtils.copyText(this, binding.tvAddress.text.toString())
        }

        binding.tvReceive.setOnClickListener {
            receiveDialog = ReceiveDialog(this, "").apply {
                show(supportFragmentManager, "receiveDialog")
            }
        }

        binding.tvSend.setOnClickListener {
            startActivity(Intent(this, SendCoinActivity::class.java).apply {
                putExtra("data", "")
            })
        }

        binding.ivAdd.setOnClickListener {
            importAssetDialog.show(supportFragmentManager, "importAssetDialog")
        }
    }
}