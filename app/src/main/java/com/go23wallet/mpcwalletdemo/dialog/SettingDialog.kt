package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSettingLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.ScreenUtils

class SettingDialog(private val mContext: Context) :
    BaseDialogFragment<DialogSettingLayoutBinding>() {

//    private val reshardingDialog: ReshardingDialog by lazy {
//        ReshardingDialog(mContext)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight(mContext) * 0.8).toInt())
    }

    override val layoutId: Int = R.layout.dialog_setting_layout

    override fun initViews(v: View?) {
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.tvResharding.setOnClickListener {
            dismissAllowingStateLoss()
            // TODO sdk set pin code dialog， then show start reshard dialog

//            reshardingDialog.show(parentFragmentManager, "reshardingDialog")
        }
    }
}