package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.content.Intent
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.Go23WalletManage
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.go23.bean.token.Token
import com.go23.bean.token.TokenListResponse
import com.go23.callback.BaseCallBack
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.SelectTokenAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSelectTokenSendLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.Constant
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.view.LoadMoreListener
import com.go23wallet.mpcwalletdemo.wallet.AddATokenActivity

class SelectTokenSendDialog(private val mContext: Context) :
    BaseDialogFragment<DialogSelectTokenSendLayoutBinding>() {

    private var page = 1

    private var listener: LoadMoreListener? = null

    private var mAdapter: SelectTokenAdapter? = null
    override val layoutId: Int = R.layout.dialog_select_token_send_layout

    var callback: (token: Token) -> Unit = {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        getData()
        UpdateDataLiveData.liveData.observe(this) {
            if (it == 1) {
                getData()
                UpdateDataLiveData.clearType()
            }
        }
        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            if (mAdapter == null) {
                mAdapter = SelectTokenAdapter()
            }
            adapter = mAdapter
        }

        mAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = mAdapter?.getItem(position) ?: return
                callback.invoke(item)
                dismissAllowingStateLoss()
            }
        })

        viewBinding.tvAdd.setOnClickListener {
            startActivity(Intent(mContext, AddATokenActivity::class.java))
        }

        listener?.let { viewBinding.recyclerView.removeOnScrollListener(it) }
        listener = object : LoadMoreListener(viewBinding.recyclerView.layoutManager) {
            override fun onLoadMore() {
                page++
                getData()
            }
        }
        viewBinding.recyclerView.addOnScrollListener(listener as LoadMoreListener)
    }

    private fun getData() {
        Go23WalletManage.getInstance().requestUserTokens(
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            page, Constant.PAGE_SIZE,
            object : BaseCallBack<TokenListResponse> {
                override fun success(data: TokenListResponse?) {
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