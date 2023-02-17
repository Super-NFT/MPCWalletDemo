package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.Go23WalletManage
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.go23.bean.chain.DefaultUserChainResponse
import com.go23.callback.BaseCallBack
import com.go23.bean.chain.UserChain
import com.go23.bean.user.Balance
import com.go23.bean.user.BalanceResponse
import com.go23.bean.user.MerchantResponse
import com.go23.bean.walletinfo.WalletInfo
import com.go23.bean.walletinfo.WalletInfoResponse
import com.go23.callback.Go23WalletCallBack
import com.go23.callback.MerchantCodeCallBack
import com.go23.callback.ReShardingCallBack
import com.go23.callback.RestoreCallBack
import com.go23.enumclass.OperationType
import com.go23.enumclass.VerifyCodeType
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.data.UserInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivityWalletBinding
import com.go23wallet.mpcwalletdemo.dialog.*
import com.go23wallet.mpcwalletdemo.ext.hideOrShowValue
import com.go23wallet.mpcwalletdemo.ext.parseAddress
import com.go23wallet.mpcwalletdemo.fragment.NFTFragment
import com.go23wallet.mpcwalletdemo.fragment.TokenFragment
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class WalletActivity : BaseActivity<ActivityWalletBinding>() {

    private val tabList = mutableListOf<String>()
    private val fragments = mutableListOf<BaseFragment<out ViewBinding>>()
    private var tabAdapter: TabFragmentAdapter? = null

    private var userChain: UserChain? = null

    private var walletInfo: WalletInfo? = null

    private var balanceData: Balance? = null

    private var isBalanceShow = true

    private var userInfo: UserInfo? = null

    private var chooseMainnetDialog: ChooseMainnetDialog? = null

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
        userInfo = intent.getParcelableExtra("user_info")
        if (userInfo == null || userInfo?.uniqueId.isNullOrEmpty() || (userInfo?.email.isNullOrEmpty() && userInfo?.phone.isNullOrEmpty())) {
            finish()
            return
        }
        showProgress()
        setListener()
        initUserInfo()
    }

    private fun initUserInfo() {
        userInfo?.let {
            binding.tvEmail.text = it.nickname
            GlideUtils.loadImg(
                this@WalletActivity,
                it.avatar,
                binding.ivAvatar
            )
            initData(it)
        }
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

    private fun initData(userInfo: UserInfo) {
        Go23WalletManage.getInstance()
            .setUniqueId(userInfo.uniqueId)
            .setEmail(userInfo.email)
            .setPhoneAndDialCode(userInfo.phone, userInfo.dialCode)
            .start(this@WalletActivity, object : Go23WalletCallBack {
                override fun reStore(p0: MutableList<WalletInfo>?) {
                    accountVerifyDialog.setDialogType(OperationType.RECOVER)
                    accountVerifyDialog.setUserInfo(userInfo)
                    accountVerifyDialog.show(supportFragmentManager, "")
                    accountVerifyDialog.callback = { code: String?, account: String ->
                        code?.let {
                            if (it.isEmpty()) {
                                Go23WalletManage.getInstance()
                                    .verifyCode(if (RegexUtils.isEmail(account)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
                                        OperationType.RECOVER, object : MerchantCodeCallBack {
                                            override fun success() {
                                                CustomToast.showShort(R.string.lite_verify_code_sent)
                                            }

                                            override fun failed() {
                                                CustomToast.showShort(R.string.lite_send_code_fail)
                                            }

                                        })
                                return@let
                            }
                            showProgress()
                            Go23WalletManage.getInstance()
                                .startReStore(
                                    this@WalletActivity,
                                    it,
                                    if (RegexUtils.isEmail(account)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
                                    object : RestoreCallBack {
                                        override fun success() {
                                            successDialog.show(
                                                supportFragmentManager,
                                                "successDialog"
                                            )
                                        }

                                        override fun failed() {
                                            accountVerifyDialog.setDialogType(OperationType.RESHADING)
                                            accountVerifyDialog.setUserInfo(userInfo)
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
                                            dismissProgress()
                                            CustomToast.showShort(R.string.lite_verify_code_fail)
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
            tabList.add(getString(R.string.lite_tokens))
            tabList.add(getString(R.string.lite_nfts))
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
            .requestDefaultUserChain(
                info.wallet_address,
                object : BaseCallBack<DefaultUserChainResponse> {
                    override fun success(data: DefaultUserChainResponse?) {
                        data?.data?.let {
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
                                            balanceData = data?.data
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
                                    CustomToast.showShort(R.string.lite_resharding_fail)
                                }

                                override fun reShardingForEmail() {
                                    toReSharding()
                                }

                                override fun emailVerifyFailed() {
                                    dismissProgress()
                                    CustomToast.showShort(R.string.lite_verify_code_fail)
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

    private fun toReshardingForEmail(code: String, account: String) {
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
                            if (RegexUtils.isEmail(account)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
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
                                    CustomToast.showShort(R.string.lite_resharding_fail)
                                }

                                override fun reShardingForEmail() {
                                    toReSharding()
                                }

                                override fun emailVerifyFailed() {
                                    dismissProgress()
                                    CustomToast.showShort(R.string.lite_verify_code_fail)
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
                        CustomToast.showShort(R.string.lite_resharding_fail)
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
        accountVerifyDialog.setDialogType(OperationType.RESHADING)
        userInfo?.let { accountVerifyDialog.setUserInfo(it) }
        accountVerifyDialog.show(supportFragmentManager, "emailVerifyDialog")
        accountVerifyDialog.callback = { code: String?, account: String ->
            code?.let {
                if (it.isEmpty()) {
                    Go23WalletManage.getInstance()
                        .verifyCode(if (RegexUtils.isEmail(account)) VerifyCodeType.EMAIL else VerifyCodeType.PHONE,
                            OperationType.RESHADING,
                            object : MerchantCodeCallBack {
                                override fun success() {
                                    CustomToast.showShort(R.string.lite_verify_code_sent)
                                }

                                override fun failed() {
                                    CustomToast.showShort(R.string.lite_send_code_fail)
                                }

                            })
                    return@let
                } else {
                    toReshardingForEmail(it, account)
                }
            }
        }
    }

    private fun setListener() {
        binding.appbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            binding.refreshView.isEnabled = verticalOffset >= 0
            val color = if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                getColor(R.color.white)
            } else {
                getColor(R.color.color_FAFAFA)
            }
            binding.toolbar.setBackgroundColor(color)
            binding.layoutInfo.setBackgroundColor(color)
            window.statusBarColor = color
            try {
                val offset = appBarLayout.totalScrollRange - abs(verticalOffset)
                fragments.forEach { fragment ->
                    fragment.updateOffset(offset)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
        successDialog.callback = {
            geWalletInfo()
        }
        binding.icMore.setOnClickListener {
            settingDialog.show(supportFragmentManager, "settingDialog")
            settingDialog.callback = {
                toReshardingForPinCode()
            }
        }

        binding.ivHideShow.setOnClickListener {
            isBalanceShow = !isBalanceShow
            binding.ivHideShow.setImageResource(if (isBalanceShow) R.drawable.icon_balance_show else R.drawable.icon_balance_hide)
            binding.tvTotalBalance.text =
                balanceData?.balance?.hideOrShowValue(isBalanceShow, userChain?.symbol)
            binding.tvTotalBalanceValue.text =
                if (isBalanceShow)
                    "$${balanceData?.balance_u?.hideOrShowValue(true)}"
                else balanceData?.balance_u?.hideOrShowValue(false)

            binding.tvTotalBalanceValue.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = if (isBalanceShow) SizeUtils.dp2px(8f) else 0
            }

            if (fragments.size > 0) {
                (fragments[0] as TokenFragment).showOrHideBalance(isBalanceShow)
            }
        }

        binding.tvChainAddress.setOnClickListener {
            chooseMainnetDialog = ChooseMainnetDialog(this).apply {
                show(supportFragmentManager, "chooseMainnetDialog")
                callback = {
                    userChain = it
                    GlideUtils.loadImg(this@WalletActivity, it.image_url, binding.ivChainIcon)
                    binding.tvChainAddress.text = it.name
                    UserWalletInfoManager.serUserChain(it)
                    walletInfo?.let { it1 -> loadData(it1) }
                    tabAdapter?.notifyDataSetChanged()
                }

            }
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