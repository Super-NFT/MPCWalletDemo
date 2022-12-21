package com.go23wallet.mpcwallet.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwallet.R
import com.go23wallet.mpcwallet.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwallet.databinding.DialogStartReshardLayoutBinding

class StartReshardDialog(private val mContext: Context) :
    BaseDialogFragment<DialogStartReshardLayoutBinding>() {

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    private val setPinCodeDialog: SetPinCodeDialog by lazy {
        SetPinCodeDialog(mContext)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setGravity(Gravity.CENTER)
    }

    override val layoutId: Int = R.layout.dialog_start_reshard_layout

    override fun initViews(v: View?) {
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvStartLoading.setOnClickListener {
            if (viewBinding.tvStartLoading.text == getString(R.string.loading)) {
                Toast.makeText(mContext, getString(R.string.resharding_loading), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            viewBinding.tvStartLoading.text = getString(R.string.loading)
            // TODO
            mHandler.postDelayed({
                setPinCodeDialog.setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
                setPinCodeDialog.show(parentFragmentManager, "setPinCodeDialog")
                dismissAllowingStateLoss()
            }, 2000)

        }

    }
}