package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.os.CountDownTimer
import android.view.*
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogAccountVerifyLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.view.InputCodeView.OnCodeCompleteListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AccountVerifyDialog(private val mContext: Context, private var dialogType: Int = 0) :
    BaseDialogFragment<DialogAccountVerifyLayoutBinding>() {

    private var mCountDownTimer: CountDownTimer? = null

    override val layoutId: Int = R.layout.dialog_account_verify_layout

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
        val accountStr = SPUtils.getInstance().getString("account")
        if (RegexUtils.isEmail(accountStr)) {
            viewBinding.llVerify.setTextLen(6)
            viewBinding.tvSendTips.text = getString(R.string.send_email_code)
            viewBinding.hasSendTips.text = getString(R.string.has_send_email_tips)
        } else {
            viewBinding.llVerify.setTextLen(4)
            viewBinding.tvSendTips.text = getString(R.string.send_sms_code)
            viewBinding.hasSendTips.text = getString(R.string.has_send_sms_tips)
        }
        viewBinding.tvAccount.text = accountStr
        viewBinding.ivBack.setOnClickListener {
            callback.invoke(null)
            dismissAllowingStateLoss()
        }
        viewBinding.llVerify.setOnCodeCompleteListener(object : OnCodeCompleteListener {
            override fun inputCodeComplete(verificationCode: String?) {
                verifyCode = verificationCode
                callback.invoke(verifyCode)
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
                lifecycleScope.launch {
                    delay(1000)
                    countDown()
                }
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
                lifecycleScope.launch {
                    delay(1000)
                    countDown()
                }
            }
        }
    }

    private fun countDown() {
        if(viewBinding.tvResend.isEnabled) {
            mCountDownTimer = object : CountDownTimer(60 * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    viewBinding.tvResend.isEnabled = false
                    viewBinding.tvResend.text = "wait ${millisUntilFinished / 1000}s"
                }

                override fun onFinish() {
                    viewBinding.tvResend.isEnabled = true
                    viewBinding.tvResend.text = getString(R.string.resend)
                }
            }
            mCountDownTimer?.start()
        }
    }

    fun clearText() {
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
        mCountDownTimer?.cancel()
    }

    companion object {
        const val TYPE_SEND = 0
        const val TYPE_VERITY = 1
    }

}