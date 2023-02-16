package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.adapter.CountryCodeAdapter
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogAreaCodeLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.utils.RequestUtils

class CountryCodeDialog(val mContext: Context) : BaseDialogFragment<DialogAreaCodeLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_area_code_layout

    var callback: String.() -> Unit = {}

    private var mAdapter: CountryCodeAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        viewBinding.progress.visibility = View.VISIBLE
        viewBinding.progress.show()
        RequestUtils.getCountryCode {
            viewBinding.progress.visibility = View.GONE
            viewBinding.progress.hide()
            if (it.code == "0") {
                mAdapter?.setNewInstance(it.data)
            } else {
                CustomToast.showShort(it.message)
            }
        }
        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            if (mAdapter == null) {
                mAdapter = CountryCodeAdapter()
            }
            adapter = mAdapter
        }

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            mAdapter?.data?.let {
                callback.invoke(it[position].dial_code)
                dismissAllowingStateLoss()
            }
        }
    }
}