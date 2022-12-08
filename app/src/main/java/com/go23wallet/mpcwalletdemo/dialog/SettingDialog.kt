package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSettingLayoutBinding

class SettingDialog(mContext: Context): BaseDialogFragment<DialogSettingLayoutBinding>() {

    private val reshardingDialog: ReshardingDialog by lazy {
        ReshardingDialog(mContext)
    }

    override val layoutId: Int = R.layout.dialog_setting_layout

    override fun initViews(v: View?) {
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.tvResharding.setOnClickListener {
            dismissAllowingStateLoss()
            reshardingDialog.show(parentFragmentManager, "reshardingDialog")
        }
    }
}