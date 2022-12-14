package com.go23wallet.mpcwalletdemo.base

import android.content.Context
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import android.content.DialogInterface.OnShowListener
import android.content.DialogInterface
import com.go23wallet.mpcwalletdemo.R
import android.view.Gravity
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.databinding.DialogBaseAnimateProgressBinding

class AnimateProgressDialog : BaseDialogFragment<DialogBaseAnimateProgressBinding>(),
    OnShowListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    override val layoutId = R.layout.dialog_base_animate_progress

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setGravity(Gravity.CENTER)
        isCancelable = true
        setCanceledOnTouchOutside(false)
    }

    override fun initViews(v: View?) {
        if (TextUtils.isEmpty(mTip)) {
            viewBinding.tvTip.visibility = View.GONE
        } else {
            viewBinding.tvTip.text = mTip
            viewBinding.tvTip.visibility = View.VISIBLE
        }
    }

    override fun onShow(dialog: DialogInterface) {
        viewBinding.progress.show()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewBinding.progress.hide()
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding.progress.hide()
    }

    private var mTip: String? = null
    fun setMessage(msg: String?) {
        mTip = msg
        if (isLateinited()) {
            if (TextUtils.isEmpty(mTip)) {
                viewBinding.tvTip.visibility = View.GONE
            } else {
                viewBinding.tvTip.text = mTip
                viewBinding.tvTip.visibility = View.VISIBLE
            }
        }
    }
}