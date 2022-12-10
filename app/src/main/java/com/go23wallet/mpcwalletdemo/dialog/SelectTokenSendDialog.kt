package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.MainnetAdapter
import com.go23wallet.mpcwalletdemo.adapter.SelectTokenAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogChooseMainnetLayoutBinding
import com.go23wallet.mpcwalletdemo.databinding.DialogSelectTokenSendLayoutBinding
import com.go23wallet.mpcwalletdemo.databinding.DialogSettingLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.ScreenUtils

class SelectTokenSendDialog(private val mContext: Context) :
    BaseDialogFragment<DialogSelectTokenSendLayoutBinding>() {

    private var mAdapter: SelectTokenAdapter? = null
    override val layoutId: Int = R.layout.dialog_select_token_send_layout

    var callback: (string: String) -> Unit = {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight(context) * 0.8).toInt())
    }


    override fun initViews(v: View?) {
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
            }
        })
    }
}