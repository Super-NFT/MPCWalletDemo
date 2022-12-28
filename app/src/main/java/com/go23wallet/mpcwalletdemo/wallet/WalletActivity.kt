package com.go23wallet.mpcwalletdemo.wallet

import android.content.Intent
import android.media.tv.TvRecordingClient.RecordingCallback
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.coins.app.BaseCallBack
import com.coins.app.C
import com.coins.app.Go23WalletCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.chain.UserChain
import com.coins.app.bean.chain.UserChainResponse
import com.coins.app.bean.user.BalanceResponse
import com.coins.app.bean.user.UserResponse
import com.coins.app.bean.walletinfo.WalletInfo
import com.coins.app.bean.walletinfo.WalletInfoResponse
import com.coins.app.callback.MPCCallBack
import com.coins.app.callback.RecoverCallBack
import com.coins.app.manage.Go23WalletChainManage
import com.coins.app.util.OkhttpUtil
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.TabFragmentAdapter
import com.go23wallet.mpcwalletdemo.base.BaseActivity
import com.go23wallet.mpcwalletdemo.data.ChainTokenInfo
import com.go23wallet.mpcwalletdemo.databinding.ActivityWalletBinding
import com.go23wallet.mpcwalletdemo.dialog.*
import com.go23wallet.mpcwalletdemo.fragment.NFTFragment
import com.go23wallet.mpcwalletdemo.fragment.TokenFragment
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.Constant
import com.go23wallet.mpcwalletdemo.utils.CopyUtils
import com.go23wallet.mpcwalletdemo.utils.GlideUtils
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody


class WalletActivity : BaseActivity<ActivityWalletBinding>() {

    private val tabList = mutableListOf<String>()
    private val fragments = mutableListOf<Fragment>()
    private var tabAdapter: TabFragmentAdapter? = null

    private var userChains: MutableList<UserChain>? = null
    private var userChain: UserChain? = null

    private var walletInfo: WalletInfo? = null

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

    private val successDialog by lazy {
        SuccessDialog(this, "Set Pincode")
    }

    private var receiveDialog: ReceiveDialog? = null

    override val layoutRes: Int = R.layout.activity_wallet

    override fun initViews(savedInstanceState: Bundle?) {
        binding.tvEmail.text = Constant.emailStr
        GlideUtils.loadImg(
            this@WalletActivity,
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F14297516724%2F641&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1673507770&t=02715a701dcbd42df9024be0da7c4f62",
            binding.ivAvatar
        )
        showProgress()
        initData()
        binding.refreshView.setOnRefreshListener {
            walletInfo?.let {
                loadData(it)
//                UpdateDataLiveData.setUpdateType(1)
//                UpdateDataLiveData.setUpdateType(2)
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
        Go23WalletManage.getInstance().setUniqueId(Constant.emailStr).setEmail(Constant.emailStr)
            .start(this@WalletActivity, object : Go23WalletCallBack {
                override fun recover() {
                    Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<UserResponse?> ->
                        val `object` = JsonObject()
                        `object`.addProperty("type", "recover")
                        val body = RequestBody.create(
                            "application/json".toMediaTypeOrNull(), `object`.toString()
                        )
                        val request: Request = Request.Builder()
                            .url(C.GAME_CENTER_URL + "/v1/common/email_code")
                            .addHeader(
                                "Authorization",
                                "Bearer " + Go23WalletManage.getInstance().gameCenterToken
                                    .access_token
                            )
                            .addHeader("Uuid", Go23WalletManage.getInstance().user.uuid)
                            .post(body)
                            .build()
                        try {
                            val response =
                                OkhttpUtil.getInstance().okHttpClient.newCall(request)
                                    .execute()
                            val result =
                                if (response.body != null) response.body!!.string() else ""
                            val userResponse =
                                Gson().fromJson(result, UserResponse::class.java)
                            emitter.onNext(userResponse)
                        } catch (e: Exception) {
                            emitter.onError(e)
                        } finally {
                            emitter.onComplete()
                        }
                    } as ObservableOnSubscribe<UserResponse?>).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<UserResponse?> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onError(e: Throwable) {}
                            override fun onComplete() {}
                            override fun onNext(t: UserResponse) {
                                dismissProgress()
                                forgetPswDialog.show(supportFragmentManager, "")
                                forgetPswDialog.callback = {
                                    it?.let {
                                        Go23WalletManage.getInstance()
                                            .startRecover(
                                                this@WalletActivity,
                                                "111111",
                                                object : RecoverCallBack {
                                                    override fun success() {
                                                        geWalletInfo()
                                                    }

                                                    override fun failed() {
                                                        ToastUtils.showShort("Verify code error， please reenter")
                                                        forgetPswDialog.show(
                                                            supportFragmentManager,
                                                            ""
                                                        )
                                                    }

                                                    override fun reSharding() {

                                                    }
                                                })
                                    } ?: kotlin.run {
                                        dismissProgress()
                                    }
                                }
                            }
                        })
                }

                override fun success() {
                    geWalletInfo()
                }

                override fun failed() {
                    dismissProgress()
                }

            })
    }

    private fun geWalletInfo() {
        Go23WalletManage.getInstance()
            .requestWallets(object : BaseCallBack<WalletInfoResponse?> {
                override fun success(data: WalletInfoResponse?) {
                    dismissProgress()
                    data?.data?.get(0)?.let {
                        walletInfo = it
                        binding.tvAddress.text = it.wallet_address
                        UserWalletInfoManager.setWalletInfo(it)
                        loadData(it)
                    }
                    setListener()
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
        Go23WalletChainManage.getInstance()
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
                                            binding.tvTotalBalanceValue.text =
                                                data?.data?.balance ?: "0"
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
        successDialog.callback = {
            geWalletInfo()
        }
        binding.icMore.setOnClickListener {
            settingDialog.show(supportFragmentManager, "settingDialog")
            settingDialog.callback = {
                showProgress()
                Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<UserResponse?> ->
                    val `object` = JsonObject()
                    `object`.addProperty("type", "reshare")
                    val body = RequestBody.create(
                        "application/json".toMediaTypeOrNull(), `object`.toString()
                    )
                    val request: Request = Request.Builder()
                        .url(C.GAME_CENTER_URL + "/v1/common/email_code")
                        .addHeader(
                            "Authorization",
                            "Bearer " + Go23WalletManage.getInstance().gameCenterToken
                                .access_token
                        )
                        .addHeader("Uuid", Go23WalletManage.getInstance().user.uuid)
                        .post(body)
                        .build()
                    try {
                        val response =
                            OkhttpUtil.getInstance().okHttpClient.newCall(request)
                                .execute()
                        val result =
                            if (response.body != null) response.body!!.string() else ""
                        val userResponse =
                            Gson().fromJson(result, UserResponse::class.java)
                        emitter.onNext(userResponse)
                    } catch (e: Exception) {
                        emitter.onError(e)
                    } finally {
                        emitter.onComplete()
                    }
                } as ObservableOnSubscribe<UserResponse?>).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<UserResponse?> {
                        override fun onSubscribe(d: Disposable) {}
                        override fun onError(e: Throwable) {
                            dismissProgress()
                        }

                        override fun onComplete() {}
                        override fun onNext(t: UserResponse) {
                            forgetPswDialog.show(supportFragmentManager, "forgetPswDialog")
                            forgetPswDialog.callback = {
                                it?.let {
                                    Go23WalletManage.getInstance().startReShare(
                                        this@WalletActivity,
                                        Go23WalletManage.getInstance().walletAddress,
                                        "111111",
                                        object : MPCCallBack {
                                            override fun success() {
                                                dismissProgress()
                                                successDialog.show(
                                                    supportFragmentManager,
                                                    "successDialog"
                                                )
                                            }

                                            override fun failed() {
                                                dismissProgress()
                                            }
                                        })
                                } ?: kotlin.run {
                                    dismissProgress()
                                }
                            }
                        }
                    })
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
            CopyUtils.copyText(this, binding.tvAddress.text.toString())
        }

        binding.tvReceive.setOnClickListener {
            receiveDialog = ReceiveDialog(
                this,
                binding.tvChainAddress.text.toString(),
                binding.tvAddress.text.toString()
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
                            binding.tvAddress.text.toString(),
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