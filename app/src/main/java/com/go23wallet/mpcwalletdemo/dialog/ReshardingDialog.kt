package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogReshardingLayoutBinding

class ReshardingDialog(mContext: Context) : BaseDialogFragment<DialogReshardingLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_resharding_layout

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    private val accountVerifyDialog: AccountVerifyDialog by lazy {
        AccountVerifyDialog(mContext, 1)
    }

    private val startReshardDialog: StartReshardDialog by lazy {
        StartReshardDialog(mContext)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setGravity(Gravity.CENTER)
    }

    override fun initViews(v: View?) {
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.tvForget.setOnClickListener {
            accountVerifyDialog.show(parentFragmentManager, "emailVerifyDialog")
            dismissAllowingStateLoss()
        }
        viewBinding.tvVerify.setOnClickListener {
            viewBinding.tvErrorTip.visibility = View.GONE
            mHandler.postDelayed({
                startReshardDialog.show(parentFragmentManager, "startReshardDialog")
                dismissAllowingStateLoss()
            }, 1000)
        }

        viewBinding.etPinCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewBinding.tvVerify.isEnabled = (s?.length ?: 0) > 0
            }
        })
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        viewBinding.etPinCode.setText("")
    }

}