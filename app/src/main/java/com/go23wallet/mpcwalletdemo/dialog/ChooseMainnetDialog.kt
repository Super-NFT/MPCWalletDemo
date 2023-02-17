package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.Go23WalletManage
import com.blankj.utilcode.util.ScreenUtils
import com.go23.bean.chain.ChainResponse
import com.go23.bean.chain.UserChain
import com.go23.bean.chain.UserChainResponse
import com.go23.callback.BaseCallBack
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.MainnetAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogChooseMainnetLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.Constant
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.view.LoadMoreListener

class ChooseMainnetDialog(private val mContext: Context) :
    BaseDialogFragment<DialogChooseMainnetLayoutBinding>() {

    private var page = 1

    private var listener: LoadMoreListener? = null

    private var mAdapter: MainnetAdapter? = null
    override val layoutId: Int = R.layout.dialog_choose_mainnet_layout

    var callback: (data: UserChain) -> Unit = {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        getData()
        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            if (mAdapter == null) {
                mAdapter = MainnetAdapter()
            }
            adapter = mAdapter
        }

        viewBinding.recyclerView.addOnScrollListener(object :
            LoadMoreListener(viewBinding.recyclerView.layoutManager) {
            override fun onLoadMore() {
                page++
                getData()
            }
        })

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            mAdapter?.data?.get(position)?.let {
                Go23WalletManage.getInstance().setDefaultChain(
                    it.chain_id,
                    UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                    object : BaseCallBack<ChainResponse> {
                        override fun success(data: ChainResponse?) {
                            data?.let { response ->
                                if (response.code == 0) {
                                    it.isHas_default = true
                                    callback.invoke(it)
                                    dismissAllowingStateLoss()
                                    return@let
                                }
                                CustomToast.showShort(getString(R.string.lite_switch_chain_fail))
                            }
                        }

                        override fun failed() {
                            CustomToast.showShort(getString(R.string.lite_switch_chain_fail))
                        }
                    })
            }
        }
    }

    private fun getData() {
        Go23WalletManage.getInstance()
            .requestUserChains(
                UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                page, Constant.PAGE_SIZE,
                object : BaseCallBack<UserChainResponse> {
                    override fun success(data: UserChainResponse?) {
                        listener?.setIsEnd(data?.data?.list?.isEmpty() ?: true)
                        if (page == 1) {
                            mAdapter?.setNewInstance(data?.data?.list)
                        } else {
                            data?.data?.list?.let { mAdapter?.addData(it) }
                        }
                    }

                    override fun failed() {
                    }
                })
    }
}