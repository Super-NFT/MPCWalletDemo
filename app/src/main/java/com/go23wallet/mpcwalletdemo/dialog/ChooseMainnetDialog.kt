package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.chain.ChainResponse
import com.coins.app.bean.chain.UserChain
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.MainnetAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogChooseMainnetLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager

class ChooseMainnetDialog(private val mContext: Context) :
    BaseDialogFragment<DialogChooseMainnetLayoutBinding>() {

    private var mAdapter: MainnetAdapter? = null
    override val layoutId: Int = R.layout.dialog_choose_mainnet_layout

    var callback: (data: UserChain) -> Unit = {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    private var userChains = mutableListOf<UserChain>()
    fun setChainList(list: MutableList<UserChain>) {
        userChains = list
    }

    override fun initViews(v: View?) {
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

        mAdapter?.setNewInstance(userChains)

        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = userChains?.get(position) ?: return

                Go23WalletManage.getInstance().setDefaultChain(
                    item.chain_id,
                    UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                    object : BaseCallBack<ChainResponse> {
                        override fun success(p0: ChainResponse?) {
                            userChains?.forEach {
                                it.isHas_default = false
                            }
                            item.isHas_default = true
                            callback.invoke(item)
                            dismissAllowingStateLoss()
                        }

                        override fun failed() {
                        }
                    })
            }
        })
    }
}