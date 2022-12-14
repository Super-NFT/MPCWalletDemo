package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletChainManage
import com.coins.app.Go23WalletInfoManage
import com.coins.app.Go23WalletManage
import com.coins.app.Go23WalletUserManage
import com.coins.app.bean.chain.UserChain
import com.coins.app.bean.chain.UserChainResponse
import com.coins.app.bean.user.BalanceResponse
import com.coins.app.bean.user.UserResponse
import com.coins.app.bean.walletinfo.WalletInfo
import com.coins.app.bean.walletinfo.WalletInfoResponse
import com.coins.app.entity.gamecenter.GameCenterTokenResponse
import com.coins.app.util.GameCenterTokenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.databinding.ActivityWalletBinding
import com.go23wallet.mpcwalletdemo.dialog.*
import com.go23wallet.mpcwalletdemo.fragment.NFTFragment
import com.go23wallet.mpcwalletdemo.fragment.TokenFragment
import com.go23wallet.mpcwalletdemo.livedata.TokenListLiveData
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class WalletActivity : BaseActivity<ActivityWalletBinding>() {

    private val tabList = mutableListOf<String>()
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    private var userChains: MutableList<UserChain>? = null

    private val chooseMainnetDialog: ChooseMainnetDialog by lazy {
        ChooseMainnetDialog(this)
    }

    private val forgetPswDialog: ForgetPswDialog by lazy {
        ForgetPswDialog(this)
    }

    private val settingDialog by lazy {
        SettingDialog(this)
    }

    private val importAssetDialog by lazy {
        ImportAssetDialog(this)
    }

    private var emailStr = "v1@coins.ph"

    private var receiveDialog: ReceiveDialog? = null

    override val layoutRes: Int = R.layout.activity_wallet

    override fun initViews(savedInstanceState: Bundle?) {
        binding.tvEmail.text = emailStr
        GlideUtils.loadImg(
            this@WalletActivity,
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F14297516724%2F641&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1673507770&t=02715a701dcbd42df9024be0da7c4f62",
            binding.ivAvatar
        )
        showProgress()
        initData()
        binding.refreshView.setOnRefreshListener {
            initData()
        }
        binding.tvEmail.text = emailStr
        GlideUtils.loadImg(
            this@WalletActivity,
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F14297516724%2F641&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1673507770&t=02715a701dcbd42df9024be0da7c4f62",
            binding.ivAvatar
        )
        showProgress()
        initData()
        binding.refreshView.setOnRefreshListener {
            initData()
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            delay(2000)
            binding.refreshView.isRefreshing = false
            runOnUiThread {
                val s = "v1@coins.ph"
                Go23WalletManage.getInstance().build(applicationContext, "1", "40ad7c25")
                Go23WalletManage.getInstance().setUniqueId(s).email = s
                Go23WalletUserManage.getInstance().register(object : BaseCallBack<UserResponse> {
                    override fun success(data: UserResponse) {
                        Go23WalletInfoManage.getInstance()
                            .requestWallets(object : BaseCallBack<WalletInfoResponse?> {
                                override fun success(data: WalletInfoResponse?) {
                                    if (data?.data == null) {
                                        Go23WalletUserManage.getInstance().createKey {
                                            Log.e("哈哈哈",it.msg)
                                        }
                                    } else {
                                        Go23WalletInfoManage.getInstance()
                                            .requestWallets(object : BaseCallBack<WalletInfoResponse?> {
                                                override fun success(data: WalletInfoResponse?) {
                                                    data
                                                }
                                                override fun failed() {}
                                            })
                                    }
                                }

                                override fun failed() {}
                            })
                    }

                    override fun failed() {}

                })
            }
        }
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

    private fun initData(info: WalletInfo) {
        Go23WalletChainManage.getInstance()
            .requestUserChains(info.id, 1, 20, object : BaseCallBack<UserChainResponse> {
                override fun success(data: UserChainResponse?) {
                    userChains = data?.data?.list
                    userChains?.find {
                        it.has_default == 1
                    }?.let {
                        UserWalletInfoManager.setUserChainId(it.block_chain_id)
                        binding.tvChainAddress.text = it.name
                        GlideUtils.loadImg(
                            this@WalletActivity, it.image_url, binding.ivChainIcon
                        )
                        initView()
                        Go23WalletUserManage.getInstance()
                            .requestPlatformBalance(
                                it.block_chain_id,
                                info.addr,
                                object : BaseCallBack<BalanceResponse> {
                                    override fun success(data: BalanceResponse?) {
                                        binding.tvTotalBalanceValue.text =
                                            "$${data?.data?.balance ?: 0.00}"
                                    }

                                    override fun failed() {
                                    }
                                })
                    }
                }

                override fun failed() {
                }
            })


    }

    private fun setListener() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.icMore.setOnClickListener {
            settingDialog.show(supportFragmentManager, "settingDialog")
        }

        binding.tvChainAddress.setOnClickListener {
            userChains?.let {
                chooseMainnetDialog.apply {
                    setChainList(it)
                    show(supportFragmentManager, "chooseMainnetDialog")
                }
            }

        }

        chooseMainnetDialog.callback = {
            GlideUtils.loadImg(this, it.image_url, binding.ivChainIcon)
            binding.tvChainAddress.text = it.name
            UserWalletInfoManager.setUserChainId(it.block_chain_id)
            UpdateDataLiveData.liveData.postValue(1)
        }

        binding.tvAddress.setOnClickListener {
            CopyUtils.copyText(this, binding.tvAddress.text.toString())
        }

        binding.tvReceive.setOnClickListener {
            receiveDialog = ReceiveDialog(this, binding.tvChainAddress.text.toString()).apply {
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