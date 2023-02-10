package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPUtils
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.chain.UserChain
import com.coins.app.bean.chain.UserChainResponse
import com.coins.app.bean.user.BalanceResponse
import com.coins.app.bean.user.MerchantResponse
import com.coins.app.bean.walletinfo.WalletInfo
import com.coins.app.bean.walletinfo.WalletInfoResponse
import com.coins.app.callback.EmailCallBack
import com.coins.app.callback.ReShardingCallBack
import com.coins.app.callback.RestoreCallBack
import com.coins.app.go23.enumclass.OperationType
import com.coins.app.go23.enumclass.VerifyCodeType
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivityWalletBinding
import com.go23wallet.mpcwalletdemo.dialog.*
import com.go23wallet.mpcwalletdemo.ext.parseAddress
import com.go23wallet.mpcwalletdemo.fragment.NFTFragment
import com.go23wallet.mpcwalletdemo.fragment.TokenFragment
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WalletActivity : BaseActivity<ActivityWalletBinding>() {

    private val tabList = mutableListOf<String>()
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    private var userChains: MutableList<UserChain>? = null
    private var userChain: UserChain? = null

    private var walletInfo: WalletInfo? = null

    private var accountStr = ""

    private val setUserDialog: SetUserDialog by lazy {
        SetUserDialog(this)
    }

    private val chooseMainnetDialog: ChooseMainnetDialog by lazy {
        ChooseMainnetDialog(this)
    }

    private val accountVerifyDialog: AccountVerifyDialog by lazy {
        AccountVerifyDialog(this)
    }

    private val settingDialog by lazy {
        SettingDialog(this)
    }

    private val importAssetDialog by lazy {
        ImportAssetDialog(this)
    }

    private val successDialog by lazy {
        SuccessDialog(this, "Set Pincode")
    }

    private var receiveDialog: ReceiveDialog? = null

    override val layoutRes: Int = R.layout.activity_wallet

    override fun initViews(savedInstanceState: Bundle?) {
        accountStr = SPUtils.getInstance().getString("account")
        showProgress()
        setListener()
        if (accountStr.isNullOrEmpty()) {
            setUserDialog.show(supportFragmentManager, "setUserDialog")
        } else {
            initUserInfo()
        }
    }

    private fun initUserInfo() {
        binding.tvEmail.text = accountStr

        GlideUtils.loadImg(
            this@WalletActivity,
            "https://d2vvute2v3y7pn.cloudfront.net/logo/Avalanche/0xe0bb6feD446A2dbb27F84D3C27C4ED8EA7603366.webp",
            binding.ivAvatar
        )
        initData()
        binding.refreshView.setOnRefreshListener {
            walletInfo?.let {
                loadData(it)
                tabAdapter?.notifyDataSetChanged()
            }
            lifecycleScope.launch {
                delay(1500)
                runOnUiThread {
                    binding.refreshView.isRefreshing = false
                }
            }
        }
    }

    private fun initData() {
        if (Validator.isEmail(accountStr)) {
            Go23WalletManage.getInstance().email = accountStr
        } else {
            val dialCode: String
            val phone: String
            if (accountStr.contains(" ")) {
                dialCode = accountStr.split(" ")[0]
                phone = accountStr.split(" ")[1]
            } else {
                dialCode = "+86"
                phone = accountStr
            }
            Go23WalletManage.getInstance().setPhoneAndDialCode(phone, dialCode)
        }
        Go23WalletManage.getInstance().setUniqueId(accountStr)
            .start(this@WalletActivity, object : Go23WalletCallBack {
                override fun reStore(p0: MutableList<WalletInfo>?) {
                    accountVerifyDialog.setDialogType(1)
                    accountVerifyDialog.show(supportFragmentManager, "")
                    accountVerifyDialog.callback = {
                        it?.let {
                            if (it.isEmpty()) {
                                Go23WalletManage.getInstance()
                                    .verifyCode(if (Validator.isEmail(accountStr)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
                                        OperationType.RECOVER, object : EmailCallBack {
                                            override fun success() {
                                                CustomToast.showShort(R.string.verify_code_sent)
                                            }

                                            override fun failed() {
                                                CustomToast.showShort(R.string.send_code_fail)
                                            }

                                        })
                                return@let
                            }
                            dismissProgress()
                            Go23WalletManage.getInstance()
                                .startReStore(
                                    this@WalletActivity,
                                    it,
                                    if (Validator.isEmail(accountStr)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
                                    object : RestoreCallBack {
                                        override fun success() {
                                            successDialog.show(
                                                supportFragmentManager,
                                                "successDialog"
                                            )
                                        }

                                        override fun failed() {
                                            accountVerifyDialog.setDialogType(1)
                                            accountVerifyDialog.show(
                                                supportFragmentManager,
                                                ""
                                            )
                                        }

                                        override fun dismiss() {
                                            dismissProgress()
                                        }

                                        override fun reSharding() {
                                            toReSharding()
                                        }

                                        override fun emailVerifyFailed() {
                                            CustomToast.showShort(R.string.verify_code_fail)
                                            accountVerifyDialog.clearText()
                                        }

                                        override fun emailVerifySuccess() {
                                            dismissProgress()
                                            accountVerifyDialog.dismissAllowingStateLoss()
                                        }
                                    })
                        } ?: kotlin.run {
                            dismissProgress()
                        }
                    }
                }

                override fun success() {
                    geWalletInfo()
                }

                override fun dismiss() {
                    dismissProgress()
                }

                override fun failed() {
                    dismissProgress()
                }

                override fun preShowCreateKeyPincodeDialog() {
                    dismissProgress()
                }

                override fun createKeySuccess(address: String?, key: String?) {
                    address?.let {
                        SPUtils.getInstance().put(address, key)
                        KeygenUtils.getInstance().uploadMerchantKey(address, key ?: "")
                        geWalletInfo()
                    }
                }
            })
    }

    private fun geWalletInfo() {
        showProgress()
        Go23WalletManage.getInstance()
            .requestWallets(object : BaseCallBack<WalletInfoResponse?> {
                override fun success(data: WalletInfoResponse?) {
                    dismissProgress()
                    data?.data?.get(0)?.let {
                        walletInfo = it
                        binding.tvAddress.text = it.wallet_address.parseAddress()
                        UserWalletInfoManager.setWalletInfo(it)
                        loadData(it)
                        val key = SPUtils.getInstance().getString(it.wallet_address)
                        if (!key.isNullOrEmpty()) {
                            lifecycleScope.launch {
                                KeygenUtils.getInstance().uploadMerchantKey(it.wallet_address, key)
                            }
                        }
                    }
                }

                override fun failed() {}
            })
    }

    private fun initView() {
        if (fragments.size == 0) {
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
    }

    private fun loadData(info: WalletInfo) {
        Go23WalletManage.getInstance()
            .requestUserChains(
                info.wallet_address,
                1,
                20,
                object : BaseCallBack<UserChainResponse> {
                    override fun success(data: UserChainResponse?) {
                        userChains = data?.data?.list
                        userChains?.find {
                            it.isHas_default
                        }?.let {
                            userChain = it
                            UserWalletInfoManager.serUserChain(it)
                            binding.tvChainAddress.text = it.name
                            GlideUtils.loadImg(
                                this@WalletActivity, it.image_url, binding.ivChainIcon
                            )
                            initView()
                            Go23WalletManage.getInstance()
                                .requestPlatformBalance(
                                    it.chain_id,
                                    info.wallet_address,
                                    object : BaseCallBack<BalanceResponse> {
                                        override fun success(data: BalanceResponse?) {
                                            binding.tvTotalBalance.text =
                                                "${data?.data?.balance ?: "0"} ${it.symbol}"
                                            binding.tvTotalBalanceValue.text =
                                                "$${data?.data?.balance_u}"
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

    private fun toReshardingForPinCode() {
        showProgress()
        KeygenUtils.getInstance().requestMerchantKey(
            Go23WalletManage.getInstance().walletAddress,
            object : BaseCallBack<MerchantResponse> {
                override fun success(data: MerchantResponse?) {
                    data?.data?.let { key ->
                        Go23WalletManage.getInstance().startReShardingForPinCode(
                            this@WalletActivity,
                            key.keygen,
                            Go23WalletManage.getInstance().walletAddress,
                            object : ReShardingCallBack {

                                override fun success(key3: String?) {
                                    //update key
                                    KeygenUtils.getInstance().updateMerchantKey(
                                        Go23WalletManage.getInstance().walletAddress,
                                        key3
                                    )
                                    dismissProgress()
                                    successDialog.show(
                                        supportFragmentManager,
                                        "successDialog"
                                    )
                                }

                                override fun failed() {
                                    dismissProgress()
                                    CustomToast.showShort(R.string.resharding_fail)
                                }

                                override fun reShardingForEmail() {
                                    toReSharding()
                                }

                                override fun emailVerifyFailed() {
                                    dismissProgress()
                                    CustomToast.showShort(R.string.verify_code_fail)
                                    accountVerifyDialog.clearText()
                                }

                                override fun emailVerifySuccess() {
                                    showProgress()
                                    accountVerifyDialog.dismissAllowingStateLoss()
                                }

                                override fun dismiss() {
                                    dismissProgress()
                                }
                            })
                    } ?: kotlin.run {
                        dismissProgress()
                    }
                }

                override fun failed() {
                    dismissProgress()
                }
            })
    }

    private fun toReshardingForEmail(code: String) {
        showProgress()
        KeygenUtils.getInstance().requestMerchantKey(
            Go23WalletManage.getInstance().walletAddress,
            object : BaseCallBack<MerchantResponse> {
                override fun success(data: MerchantResponse?) {
                    data?.data?.let { key ->
                        Go23WalletManage.getInstance().startReSharding(
                            this@WalletActivity,
                            key.keygen,
                            Go23WalletManage.getInstance().walletAddress,
                            code,
                            if (Validator.isEmail(accountStr)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
                            object : ReShardingCallBack {

                                override fun success(key3: String?) {
                                    accountVerifyDialog.dismissAllowingStateLoss()
                                    //update key
                                    KeygenUtils.getInstance().updateMerchantKey(
                                        Go23WalletManage.getInstance().walletAddress,
                                        key3
                                    )
                                    dismissProgress()
                                    successDialog.show(
                                        supportFragmentManager,
                                        "successDialog"
                                    )
                                }

                                override fun failed() {
                                    dismissProgress()
                                    CustomToast.showShort(R.string.resharding_fail)
                                }

                                override fun reShardingForEmail() {
                                    toReSharding()
                                }

                                override fun emailVerifyFailed() {
                                    dismissProgress()
                                    CustomToast.showShort(R.string.verify_code_fail)
                                    accountVerifyDialog.clearText()
                                }

                                override fun emailVerifySuccess() {
                                    if (walletInfo == null) {
                                        showProgress()
                                    }
                                    accountVerifyDialog.dismissAllowingStateLoss()
                                }

                                override fun dismiss() {
                                    dismissProgress()
                                }
                            })
                    } ?: kotlin.run {
                        CustomToast.showShort(R.string.resharding_fail)
                        dismissProgress()
                    }
                }

                override fun failed() {
                    dismissProgress()
                }
            })
    }

    private fun toReSharding() {
        showProgress()
        accountVerifyDialog.setDialogType(0)
        accountVerifyDialog.show(supportFragmentManager, "emailVerifyDialog")
        accountVerifyDialog.callback = {
            it?.let {
                if (it.isEmpty()) {
                    Go23WalletManage.getInstance()
                        .verifyCode(if (Validator.isEmail(accountStr)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
                            OperationType.RECOVER,
                            object : EmailCallBack {
                                override fun success() {
                                    CustomToast.showShort(R.string.verify_code_sent)
                                }

                                override fun failed() {
                                    CustomToast.showShort(R.string.send_code_fail)
                                }

                            })
                    return@let
                } else {
                    toReshardingForEmail(it)
                }
            }
        }
    }

    private fun setListener() {
        UpdateDataLiveData.liveData.observe(this) {
            if (it == 2) {
                binding.viewPager.currentItem = 1
                UpdateDataLiveData.clearType()
            } else if (it == 3) {
                UpdateDataLiveData.clearType()
            }
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        setUserDialog.callback = {
            accountStr = this
            SPUtils.getInstance().put("account", this)
            initUserInfo()
        }
        successDialog.callback = {
            geWalletInfo()
        }
        binding.icMore.setOnClickListener {
            settingDialog.show(supportFragmentManager, "settingDialog")
            settingDialog.callback = {
                toReshardingForPinCode()
            }
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
            userChain = it
            GlideUtils.loadImg(this, it.image_url, binding.ivChainIcon)
            binding.tvChainAddress.text = it.name
            UserWalletInfoManager.serUserChain(it)
            walletInfo?.let { it1 -> loadData(it1) }
            tabAdapter?.notifyDataSetChanged()
        }

        binding.tvAddress.setOnClickListener {
            CopyUtils.copyText(this, walletInfo?.wallet_address ?: "")
        }

        binding.tvReceive.setOnClickListener {
            receiveDialog = ReceiveDialog(
                this,
                userChain?.symbol ?: "",
                walletInfo?.wallet_address ?: ""
            ).apply {
                show(supportFragmentManager, "receiveDialog")
            }
        }

        binding.tvSend.setOnClickListener {
            userChain?.let {
                startActivity(Intent(this, SendCoinActivity::class.java).apply {
                    putExtra("token_id", 0)
                    putExtra(
                        "data",
                        ChainTokenInfo(
                            it.chain_id,
                            walletInfo?.wallet_address ?: "",
                            it.name,
                            it.symbol,
                            it.image_url,
                            ""
                        )
                    )
                })
            }

        }

        binding.ivAdd.setOnClickListener {
            importAssetDialog.show(supportFragmentManager, "importAssetDialog")
        }
    }
}