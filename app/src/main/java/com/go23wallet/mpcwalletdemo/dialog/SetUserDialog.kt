package com.go23wallet.mpcwalletdemo.dialog

import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.coins.app.util.KeyboardUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSetUserLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.Validator


class SetUserDialog(val activity: Activity) : BaseDialogFragment<DialogSetUserLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_set_user_layout

    var callback: String.() -> Unit = {}

    private var setEmail = ""
    private var verifyCode = ""

    private var mCountDownTimer: CountDownTimer? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        viewBinding.ivClose.setOnClickListener {
            activity.finish()
        }
        viewBinding.etSetEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                setEmail = s.toString()
                if (setEmail.isNullOrEmpty()) return
                val isEnable = Validator.isEmail(setEmail)
                viewBinding.tvErrorTips.visibility = if (isEnable) View.INVISIBLE else View.VISIBLE
                viewBinding.tvConfirm.isEnabled = isEnable
            }

        })
        viewBinding.etVerifyCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                verifyCode = s.toString()
            }

        })

        viewBinding.root.setOnClickListener {
            KeyboardUtils.hideKeyboard(viewBinding.root)
        }

        viewBinding.tvConfirm.setOnClickListener {
            dismissAllowingStateLoss()
            callback.invoke(setEmail)
        }

        viewBinding.tvSend.setOnClickListener {
            countDown()
        }
    }

    private fun countDown() {
        if (mCountDownTimer != null) {
            return
        } else {
            mCountDownTimer = object : CountDownTimer(60 * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    viewBinding.tvSend.isEnabled = false
                    viewBinding.tvSend.text = "${millisUntilFinished / 1000}s"
                }

                override fun onFinish() {
                    viewBinding.tvSend.isEnabled = true
                    viewBinding.tvSend.text = getString(R.string.send)
                }

            }
            mCountDownTimer?.start()
        }
    }
}