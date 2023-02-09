package com.go23wallet.mpcwalletdemo.fragment

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
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
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseFragment
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.FragmentWalletBinding
import com.go23wallet.mpcwalletdemo.dialog.*
import com.go23wallet.mpcwalletdemo.ext.parseAddress
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.*
import com.go23wallet.mpcwalletdemo.wallet.SendCoinActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WalletFragment(val mContext: Context) : BaseFragment<FragmentWalletBinding>() {

    private val tabList = mutableListOf<String>()
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    private var userChains: MutableList<UserChain>? = null
    private var userChain: UserChain? = null

    private var walletInfo: WalletInfo? = null

    private var emailStr = ""

    private val setUserDialog: SetUserDialog by lazy {
        SetUserDialog(mContext)
    }

    private val chooseMainnetDialog: ChooseMainnetDialog by lazy {
        ChooseMainnetDialog(mContext)
    }

    private val emailVerifyDialog: EmailVerifyDialog by lazy {
        EmailVerifyDialog(mContext)
    }

    private val settingDialog by lazy {
        SettingDialog(mContext)
    }

    private val importAssetDialog by lazy {
        ImportAssetDialog(mContext)
    }

    private val successDialog by lazy {
        SuccessDialog(mContext, "Set Pincode")
    }

    private var receiveDialog: ReceiveDialog? = null

    override fun initViews() {
        emailStr = SPUtils.getInstance().getString("email")
        showProgress()
        setListener()
        if (emailStr.isNullOrEmpty()) {
            setUserDialog.show(parentFragmentManager, "setUserDialog")
        } else {
            initUserInfo()
        }
    }

    private fun initUserInfo() {
        binding.tvEmail.text = emailStr

        GlideUtils.loadImg(
            mContext,
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
        Go23WalletManage.getInstance().setUniqueId(emailStr).setEmail(emailStr)
            .start(activity, object : Go23WalletCallBack {
                override fun reStore(p0: MutableList<WalletInfo>?) {
                    emailVerifyDialog.setDialogType(1)
                    emailVerifyDialog.show(parentFragmentManager, "")
                    emailVerifyDialog.callback = {
                        it?.let {
                            if (it.isEmpty()) {
                                Go23WalletManage.getInstance().verifyEmailCode(1, object :
                                    EmailCallBack {
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
                                    mContext,
                                    it,
                                    object : RestoreCallBack {
                                        override fun success() {
                                            successDialog.show(
                                                parentFragmentManager,
                                                "successDialog"
                                            )
                                        }

                                        override fun failed() {
                                            emailVerifyDialog.setDialogType(1)
                                            emailVerifyDialog.show(
                                                parentFragmentManager,
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
                                            emailVerifyDialog.clearText()
                                        }

                                        override fun emailVerifySuccess() {
                                            dismissProgress()
                                            emailVerifyDialog.dismissAllowingStateLoss()
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
            tabAdapter = TabFragmentAdapter(childFragmentManager).apply {
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
                                mContext, it.image_url, binding.ivChainIcon
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
                            mContext,
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
                                        parentFragmentManager,
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
                                    emailVerifyDialog.clearText()
                                }

                                override fun emailVerifySuccess() {
                                    showProgress()
                                    emailVerifyDialog.dismissAllowingStateLoss()
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
                        Go23WalletManage.getInstance().startReShardingForEmail(
                            mContext,
                            key.keygen,
                            Go23WalletManage.getInstance().walletAddress,
                            code,
                            object : ReShardingCallBack {

                                override fun success(key3: String?) {
                                    emailVerifyDialog.dismissAllowingStateLoss()
                                    //update key
                                    KeygenUtils.getInstance().updateMerchantKey(
                                        Go23WalletManage.getInstance().walletAddress,
                                        key3
                                    )
                                    dismissProgress()
                                    successDialog.show(
                                        parentFragmentManager,
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
                                    emailVerifyDialog.clearText()
                                }

                                override fun emailVerifySuccess() {
                                    if (walletInfo == null) {
                                        showProgress()
                                    }
                                    emailVerifyDialog.dismissAllowingStateLoss()
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
        emailVerifyDialog.setDialogType(0)
        emailVerifyDialog.show(parentFragmentManager, "emailVerifyDialog")
        emailVerifyDialog.callback = {
            it?.let {
                if (it.isEmpty()) {
                    Go23WalletManage.getInstance().verifyEmailCode(0, object : EmailCallBack {
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
            activity?.finish()
        }
        setUserDialog.callback = {
            emailStr = this
            SPUtils.getInstance().put("email", this)
            initUserInfo()
        }
        successDialog.callback = {
            geWalletInfo()
        }
        binding.icMore.setOnClickListener {
            settingDialog.show(parentFragmentManager, "settingDialog")
            settingDialog.callback = {
                toReshardingForPinCode()
            }
        }

        binding.tvChainAddress.setOnClickListener {
            userChains?.let {
                chooseMainnetDialog.apply {
                    setChainList(it)
                    show(parentFragmentManager, "chooseMainnetDialog")
                }
            }

        }

        chooseMainnetDialog.callback = {
            userChain = it
            GlideUtils.loadImg(mContext, it.image_url, binding.ivChainIcon)
            binding.tvChainAddress.text = it.name
            UserWalletInfoManager.serUserChain(it)
            walletInfo?.let { it1 -> loadData(it1) }
            tabAdapter?.notifyDataSetChanged()
        }

        binding.tvAddress.setOnClickListener {
            CopyUtils.copyText(mContext, walletInfo?.wallet_address ?: "")
        }

        binding.tvReceive.setOnClickListener {
            receiveDialog = ReceiveDialog(
                mContext,
                userChain?.symbol ?: "",
                walletInfo?.wallet_address ?: ""
            ).apply {
                show(parentFragmentManager, "receiveDialog")
            }
        }

        binding.tvSend.setOnClickListener {
            userChain?.let {
                mContext.startActivity(Intent(mContext, SendCoinActivity::class.java).apply {
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
            importAssetDialog.show(parentFragmentManager, "importAssetDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        fun newInstance(mContext: Context): BaseFragment<out ViewBinding> {
            return WalletFragment(mContext)
        }
    }

}