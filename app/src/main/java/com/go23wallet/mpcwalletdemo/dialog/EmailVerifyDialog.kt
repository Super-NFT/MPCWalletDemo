package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogForgetPswLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.view.InputCodeView.OnCodeCompleteListener

class EmailVerifyDialog(private val mContext: Context, private var dialogType: Int = 0) :
    BaseDialogFragment<DialogForgetPswLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_forget_psw_layout

    var callback: (code: String?) -> Unit = {}

    private var type = TYPE_SEND

    private var verifyCode: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    /**
     * @param type 0 resharding 1 recover
     */
    fun setDialogType(type: Int) {
        dialogType = type
    }

    override fun initViews(v: View?) {
        if (dialogType == 1) {
            viewBinding.ivBack.visibility = View.GONE
        }
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
                    CustomToast.showShort(R.string.sending)
                    return@setOnClickListener
                }
                callback.invoke("")
                type = TYPE_VERITY
                viewBinding.tvSendTips.visibility = View.INVISIBLE
                viewBinding.bottomGroup.visibility = View.VISIBLE
                viewBinding.llVerify.visibility = View.VISIBLE
                viewBinding.tvVerify.text = getString(R.string.verify)
            } else {
                if (verifyCode.isNullOrEmpty() || (verifyCode?.length ?: 0) > 6) {
                    CustomToast.showShort(R.string.verify_error)
                    return@setOnClickListener
                } else {
                    if (viewBinding.progress.visibility == View.VISIBLE) {
                        CustomToast.showShort(R.string.verifying)
                        return@setOnClickListener
                    }
                    callback.invoke(verifyCode)
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