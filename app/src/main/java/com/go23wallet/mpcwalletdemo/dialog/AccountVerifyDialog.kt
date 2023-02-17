package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.os.CountDownTimer
import android.view.*
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ScreenUtils
import com.go23.enumclass.OperationType
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.data.UserInfo
import com.go23wallet.mpcwalletdemo.databinding.DialogAccountVerifyLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.view.InputCodeView.OnCodeCompleteListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AccountVerifyDialog(private val mContext: Context) :
    BaseDialogFragment<DialogAccountVerifyLayoutBinding>() {

    private var mCountDownTimer: CountDownTimer? = null

    override val layoutId: Int = R.layout.dialog_account_verify_layout

    var callback: (code: String?, account: String) -> Unit = { code: String?, account: String -> }

    private var type = TYPE_SEND

    private var verifyCode: String? = ""

    private var emailSelect = true

    private var userInfo: UserInfo? = null

    private var dialogType: OperationType = OperationType.RECOVER

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    fun setDialogType(type: OperationType) {
        dialogType = type
    }

    fun setUserInfo(info: UserInfo) {
        userInfo = info
    }

    override fun initViews(v: View?) {
        if (dialogType == OperationType.RECOVER) {
            viewBinding.ivBack.visibility = View.GONE
        }

        userInfo?.let {
            if (it.email.isNullOrEmpty()) {
                viewBinding.tvEmailTitle.visibility = View.GONE
                viewBinding.tvPhoneTitle.visibility = View.GONE
                updateView(false)
            } else {
                if (it.phone.isNullOrEmpty()) {
                    viewBinding.tvEmailTitle.visibility = View.GONE
                    viewBinding.tvPhoneTitle.visibility = View.GONE
                } else {
                    viewBinding.tvEmailTitle.visibility = View.VISIBLE
                    viewBinding.tvPhoneTitle.visibility = View.VISIBLE
                }
                updateView(true)
            }
        }

        viewBinding.ivBack.setOnClickListener {
            callback.invoke(
                null, if (emailSelect) userInfo?.email ?: "" else userInfo?.phone ?: ""
            )
            dismissAllowingStateLoss()
        }
        viewBinding.llVerify.setOnCodeCompleteListener(object : OnCodeCompleteListener {
            override fun inputCodeComplete(verificationCode: String?) {
                verifyCode = verificationCode
                callback.invoke(
                    verifyCode, if (emailSelect) userInfo?.email ?: "" else userInfo?.phone ?: ""
                )
            }

            override fun inputCodeInput(verificationCode: String?) {
            }
        })
        viewBinding.tvVerify.setOnClickListener {
            if (type == TYPE_SEND) {
                if (viewBinding.progress.visibility == View.VISIBLE) {
                    CustomToast.showShort(R.string.lite_sending)
                    return@setOnClickListener
                }
                callback.invoke(
                    "",
                    if (emailSelect) userInfo?.email ?: "" else userInfo?.phone ?: ""
                )
                type = TYPE_VERITY
                viewBinding.tvEmailTitle.visibility = View.GONE
                viewBinding.tvPhoneTitle.visibility = View.GONE
                viewBinding.tvSendTips.visibility = View.INVISIBLE
                viewBinding.bottomGroup.visibility = View.VISIBLE
                viewBinding.llVerify.visibility = View.VISIBLE
                viewBinding.tvVerify.text = getString(R.string.lite_verify)
                lifecycleScope.launch {
                    delay(1000)
                    countDown()
                }
            } else {
                if (verifyCode.isNullOrEmpty() || (verifyCode?.length ?: 0) > 6) {
                    CustomToast.showShort(R.string.lite_verify_error)
                    return@setOnClickListener
                } else {
                    if (viewBinding.progress.visibility == View.VISIBLE) {
                        CustomToast.showShort(R.string.lite_verifying)
                        return@setOnClickListener
                    }
                    callback.invoke(
                        verifyCode,
                        if (emailSelect) userInfo?.email ?: "" else userInfo?.phone ?: ""
                    )
                }
            }
        }

        viewBinding.tvEmailTitle.setOnClickListener {
            viewBinding.tvEmailTitle.setTextColor(mContext.getColor(R.color.color_262626))
            viewBinding.tvPhoneTitle.setTextColor(mContext.getColor(R.color.color_8C8C8C))
            updateView(true)
        }

        viewBinding.tvPhoneTitle.setOnClickListener {
            viewBinding.tvEmailTitle.setTextColor(mContext.getColor(R.color.color_8C8C8C))
            viewBinding.tvPhoneTitle.setTextColor(mContext.getColor(R.color.color_262626))
            updateView(false)
        }

        viewBinding.tvResend.setOnClickListener {
            if (viewBinding.tvResend.visibility == View.VISIBLE) {
                viewBinding.llVerify.setText("")
                callback.invoke(
                    "", if (emailSelect) userInfo?.email ?: "" else userInfo?.phone ?: ""
                )
                lifecycleScope.launch {
                    delay(1000)
                    countDown()
                }
            }
        }
    }

    private fun updateView(isEmail: Boolean) {
        emailSelect = isEmail
        userInfo?.let {
            if (emailSelect) {
                viewBinding.tvAccount.text = it.email
                viewBinding.llVerify.setTextLen(6)
                viewBinding.tvSendTips.text = getString(R.string.lite_send_code, "email")
                viewBinding.hasSendTips.text = getString(R.string.lite_has_send_tips, "email")
            } else {
                viewBinding.tvAccount.text = it.phone
                viewBinding.llVerify.setTextLen(4)
                viewBinding.tvSendTips.text = getString(R.string.lite_send_code, "SMS")
                viewBinding.hasSendTips.text = getString(R.string.lite_has_send_tips, "SMS")
            }
        }
    }

    private fun countDown() {
        if (viewBinding.tvResend.isEnabled) {
            mCountDownTimer = object : CountDownTimer(60 * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    viewBinding.tvResend.isEnabled = false
                    viewBinding.tvResend.text = "wait ${millisUntilFinished / 1000}s"
                }

                override fun onFinish() {
                    viewBinding.tvResend.isEnabled = true
                    viewBinding.tvResend.text = getString(R.string.lite_resend)
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