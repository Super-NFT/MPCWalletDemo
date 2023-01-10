package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.content.Intent
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.token.Token
import com.coins.app.bean.token.TokenListResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.SelectTokenAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSelectTokenSendLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.go23wallet.mpcwalletdemo.wallet.AddATokenActivity

class SelectTokenSendDialog(private val mContext: Context) :
    BaseDialogFragment<DialogSelectTokenSendLayoutBinding>() {

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
    }

    private fun getData() {
        Go23WalletManage.getInstance().requestUserTokens(
            UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
            UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
            1, 20,
            object : BaseCallBack<TokenListResponse> {
                override fun success(data: TokenListResponse?) {
                    val list = data?.data?.list
                    mAdapter?.setNewInstance(list)
                }

                override fun failed() {
                }
            })
    }
}