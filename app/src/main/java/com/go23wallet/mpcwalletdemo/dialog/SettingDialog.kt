package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.blankj.utilcode.util.ScreenUtils
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.walletinfo.WalletInfoResponse
import com.coins.app.callback.MPCCallBack
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSettingLayoutBinding

class SettingDialog(private val mContext: Context) :
    BaseDialogFragment<DialogSettingLayoutBinding>() {

//    private val reshardingDialog: ReshardingDialog by lazy {
//        ReshardingDialog(mContext)
//    }

    var callback: () -> Unit = {}

    private val forgetPswDialog: ForgetPswDialog by lazy {
        ForgetPswDialog(mContext)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override val layoutId: Int = R.layout.dialog_setting_layout

    override fun initViews(v: View?) {
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.tvResharding.setOnClickListener {
            callback.invoke()
            dismissAllowingStateLoss()
            // TODO sdk set pin code dialog， then show start reshard dialog

//            reshardingDialog.show(parentFragmentManager, "reshardingDialog")
        }
    }
}