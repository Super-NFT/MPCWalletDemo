package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ScreenUtils
import com.coins.app.util.KeyboardUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSetUserLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.utils.Validator

class SetUserDialog(val mContext: Context) : BaseDialogFragment<DialogSetUserLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_set_user_layout

    private val areaCodeDialog: AreaCodeDialog by lazy {
        AreaCodeDialog(mContext)
    }

    private var areaCode = "+86"

    var callback: String.() -> Unit = {}

    private var inputValue = ""

    /**
     * email 0
     * sms 1
     */
    private var inputType = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }

    override fun initViews(v: View?) {
        viewBinding.etSetEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s.toString().trim()
                if (inputValue.isNullOrEmpty()) return
                val isEnable =
                    if (inputType == 0) Validator.isEmail(inputValue) else inputValue.length > 5
                viewBinding.tvErrorTips.visibility = if (isEnable) View.INVISIBLE else View.VISIBLE
                viewBinding.tvErrorTips.text =
                    getString(if (inputType == 0) R.string.email_input_error else R.string.sms_input_error)
                viewBinding.tvConfirm.isEnabled = isEnable
            }
        })

        viewBinding.tvSetEmailTip.setOnClickListener {
            inputType = 0
            viewBinding.tvAreaCode.visibility = View.GONE
            viewBinding.etSetEmail.inputType = InputType.TYPE_CLASS_TEXT
            viewBinding.tvSetEmailTip.setTextColor(resources.getColor(R.color.color_262626))
            viewBinding.tvSetSmsTip.setTextColor(resources.getColor(R.color.color_8C8C8C))
        }

        viewBinding.tvSetSmsTip.setOnClickListener {
            inputType = 1
            viewBinding.tvAreaCode.visibility = View.VISIBLE
            viewBinding.etSetEmail.inputType = InputType.TYPE_CLASS_PHONE
            viewBinding.tvSetEmailTip.setTextColor(resources.getColor(R.color.color_8C8C8C))
            viewBinding.tvSetSmsTip.setTextColor(resources.getColor(R.color.color_262626))
        }

        viewBinding.tvAreaCode.setOnClickListener {
            areaCodeDialog.show(parentFragmentManager, "areaCodeDialog")
        }

        areaCodeDialog.callback = {
            areaCode = this
            viewBinding.tvAreaCode.text = this
        }

        viewBinding.root.setOnClickListener {
            KeyboardUtils.hideKeyboard(viewBinding.root)
        }

        viewBinding.tvConfirm.setOnClickListener {
            if (NetworkUtils.isConnected()) {
                dismissAllowingStateLoss()
                callback.invoke(if (inputType == 0) inputValue else "$areaCode $inputValue")
            } else {
                CustomToast.showShort(R.string.network_error)
            }
        }
    }
}