package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogForgetPswLayoutBinding
import com.go23wallet.mpcwalletdemo.view.InputCodeView.OnCodeCompleteListener

class ForgetPswDialog(private val mContext: Context, val dialogType: Int = 0) :
    BaseDialogFragment<DialogForgetPswLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_forget_psw_layout

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    private val setPinCodeDialog: SetPinCodeDialog by lazy {
        SetPinCodeDialog(mContext)
    }

    private var type = TYPE_SEND

    private var verifyCode: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setGravity(Gravity.CENTER)
    }

    override fun initViews(v: View?) {
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.llVerify.setOnCodeCompleteListener(object : OnCodeCompleteListener {
            override fun inputCodeComplete(verificationCode: String?) {
                verifyCode = verificationCode
            }

            override fun inputCodeInput(verificationCode: String?) {
            }
        })
        viewBinding.tvVerify.setOnClickListener {
            if (type == TYPE_SEND) {
                if (viewBinding.progress.visibility == View.VISIBLE) {
                    Toast.makeText(context, R.string.sending, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                showProgress()
                mHandler.postDelayed({
                    type = TYPE_VERITY
                    hideProgress()
                    viewBinding.tvSendTips.visibility = View.GONE
                    viewBinding.bottomGroup.visibility = View.VISIBLE
                    viewBinding.llVerify.visibility = View.VISIBLE
                    viewBinding.tvVerify.text = getString(R.string.verify)
                }, 2000)
            } else {
                if (verifyCode.isNullOrEmpty()) {
                    Toast.makeText(context, R.string.verify_error, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    if (viewBinding.progress.visibility == View.VISIBLE) {
                        Toast.makeText(context, R.string.verifying, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    // TODO
                    showProgress()
                    if (dialogType == 0) {
                        // recover  set pin code
                    } else {
                        // resharding  two set pin code
                    }
                    mHandler.postDelayed({
                        hideProgress()
                        setPinCodeDialog.show(parentFragmentManager, "setPinCodeDialog")
                        dismissAllowingStateLoss()
                    }, 2000)
                }
            }
        }

        viewBinding.tvResend.setOnClickListener {
            viewBinding.llVerify.setText("")
        }
    }

    private fun showProgress() {
        viewBinding.progress.visibility = View.VISIBLE
        viewBinding.progress.show()
    }

    private fun hideProgress() {
        viewBinding.progress.visibility = View.GONE
        viewBinding.progress.hide()
    }

    companion object {
        const val TYPE_SEND = 0
        const val TYPE_VERITY = 1
    }

}