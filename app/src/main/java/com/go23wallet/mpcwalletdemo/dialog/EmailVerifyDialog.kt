package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import android.widget.Toast
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogForgetPswLayoutBinding
import com.go23wallet.mpcwalletdemo.view.InputCodeView.OnCodeCompleteListener

class EmailVerifyDialog(private val mContext: Context, val dialogType: Int = 0) :
    BaseDialogFragment<DialogForgetPswLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_forget_psw_layout

    var callback: (code: String?) -> Unit = {}

    private var type = TYPE_SEND

    private var verifyCode: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        val emailStr = SPUtils.getInstance().getString("email")
        viewBinding.tvEmail.text = emailStr
        viewBinding.ivBack.setOnClickListener {
            callback.invoke(null)
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
                callback.invoke("")
                type = TYPE_VERITY
                viewBinding.tvSendTips.visibility = View.INVISIBLE
                viewBinding.bottomGroup.visibility = View.VISIBLE
                viewBinding.llVerify.visibility = View.VISIBLE
                viewBinding.tvVerify.text = getString(R.string.verify)
            } else {
                if (verifyCode.isNullOrEmpty() && (verifyCode?.length ?: 0) > 6) {
                    Toast.makeText(context, R.string.verify_error, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    if (viewBinding.progress.visibility == View.VISIBLE) {
                        Toast.makeText(context, R.string.verifying, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    // TODO
                    if (dialogType == 0) {
                        // recover  set pin code
                        callback.invoke(verifyCode)
                    } else {
                        // resharding  two set pin code
                        callback.invoke(verifyCode)
                    }
                }
            }
        }

        viewBinding.tvResend.setOnClickListener {
            if (viewBinding.tvResend.visibility == View.VISIBLE) {
                viewBinding.llVerify.setText("")
                callback.invoke("")
            }
        }
    }

    public fun clearText() {
        viewBinding.llVerify.setText("")
    }

    private fun showProgress() {
        viewBinding.progress.visibility = View.VISIBLE
        viewBinding.progress.show()
    }

    private fun hideProgress() {
        viewBinding.progress.visibility = View.GONE
        viewBinding.progress.hide()
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        type = TYPE_SEND
        viewBinding.llVerify.setText("")
    }

    companion object {
        const val TYPE_SEND = 0
        const val TYPE_VERITY = 1
    }

}