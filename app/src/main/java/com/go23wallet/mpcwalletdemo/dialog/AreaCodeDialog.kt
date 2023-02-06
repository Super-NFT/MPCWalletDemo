package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.AreaCodeAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogAreaCodeLayoutBinding

class AreaCodeDialog(val mContext: Context) : BaseDialogFragment<DialogAreaCodeLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_area_code_layout

    var callback: String.() -> Unit = {}

    private var mAdapter: AreaCodeAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            if (mAdapter == null) {
                mAdapter = AreaCodeAdapter()
            }
            adapter = mAdapter
        }

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val data = mAdapter?.data ?: return@setOnItemClickListener
            callback.invoke(data[position].name)
        }
    }
}