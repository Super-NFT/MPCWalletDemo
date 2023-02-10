package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.View
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.RomUtils
import com.blankj.utilcode.util.ScreenUtils
import com.coins.app.util.KeyboardUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSetUserLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CustomToast

class SetUserDialog(val mContext: Context) : BaseDialogFragment<DialogSetUserLayoutBinding>() {

    companion object {
        const val TYPE_EMAIL = 0
        const val TYPE_SMS = 1
    }

    override val layoutId: Int = R.layout.dialog_set_user_layout

    private val countryCodeDialog: CountryCodeDialog by lazy {
        CountryCodeDialog(mContext)
    }

    private var countryCode = "+86"

    var callback: String.() -> Unit = {}

    private var inputValue = ""

    /**
     * TYPE_EMAIL
     * TYPE_SMS
     */
    private var inputType = TYPE_EMAIL

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        viewBinding.etSetEmailSms.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString().trim()
                if (inputValue.isNullOrEmpty()) {
                    viewBinding.tvErrorTips.visibility = View.INVISIBLE
                    return
                }
                val isEnable = if (inputType == TYPE_EMAIL) {
                    RegexUtils.isEmail(inputValue)
                } else inputValue.length > 5
                viewBinding.tvErrorTips.visibility =
                    if (isEnable) View.INVISIBLE else View.VISIBLE
                viewBinding.tvErrorTips.text =
                    getString(if (inputType == TYPE_EMAIL) R.string.email_input_error else R.string.sms_input_error)
                viewBinding.tvConfirm.isEnabled = isEnable
            }
        })

        viewBinding.tvSetEmailTip.setOnClickListener {
            updateView(TYPE_EMAIL)
        }

        viewBinding.tvSetSmsTip.setOnClickListener {
            updateView(TYPE_SMS)
        }

        viewBinding.tvAreaCode.setOnClickListener {
            countryCodeDialog.show(parentFragmentManager, "areaCodeDialog")
        }

        countryCodeDialog.callback = {
            countryCode = this
            viewBinding.tvAreaCode.text = this
        }

        viewBinding.root.setOnClickListener {
            KeyboardUtils.hideKeyboard(viewBinding.root)
        }

        viewBinding.tvConfirm.setOnClickListener {
            if (NetworkUtils.isConnected()) {
                dismissAllowingStateLoss()
                callback.invoke(if (inputType == TYPE_EMAIL) inputValue else "$countryCode $inputValue")
            } else {
                CustomToast.showShort(R.string.network_error)
            }
        }
    }

    private fun updateView(type: Int) {
        if (inputType != type) {
            viewBinding.etSetEmailSms.setText("")
            inputType = type
            viewBinding.tvAreaCode.visibility = if (type == TYPE_EMAIL) View.GONE else View.VISIBLE
            if (RomUtils.isSamsung()) {
                viewBinding.etSetEmailSms.inputType = InputType.TYPE_CLASS_TEXT
                viewBinding.etSetEmailSms.keyListener =
                    DigitsKeyListener.getInstance(if (type == TYPE_EMAIL) "" else "0123456789")
            } else {
                viewBinding.etSetEmailSms.inputType =
                    if (type == TYPE_EMAIL) InputType.TYPE_CLASS_TEXT else InputType.TYPE_CLASS_NUMBER
            }
            viewBinding.tvSetEmailTip.setTextColor(resources.getColor(if (type == TYPE_EMAIL) R.color.color_262626 else R.color.color_8C8C8C))
            viewBinding.tvSetSmsTip.setTextColor(resources.getColor(if (type == TYPE_EMAIL) R.color.color_8C8C8C else R.color.color_262626))
        }
    }
}