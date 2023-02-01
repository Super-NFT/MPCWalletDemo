package com.go23wallet.mpcwalletdemo.dialog

import android.app.Activity
import android.content.Context
import android.text.Editable
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


class SetUserDialog(val activity: Context) : BaseDialogFragment<DialogSetUserLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_set_user_layout

    var callback: String.() -> Unit = {}

    private var setEmail = ""

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
                setEmail = s.toString().trim()
                if (setEmail.isNullOrEmpty()) return
                val isEnable = Validator.isEmail(setEmail)
                viewBinding.tvErrorTips.visibility = if (isEnable) View.INVISIBLE else View.VISIBLE
                viewBinding.tvConfirm.isEnabled = isEnable
            }

        })

        viewBinding.root.setOnClickListener {
            KeyboardUtils.hideKeyboard(viewBinding.root)
        }

        viewBinding.tvConfirm.setOnClickListener {
            if (NetworkUtils.isConnected()) {
                dismissAllowingStateLoss()
                callback.invoke(setEmail)
            } else {
                CustomToast.showShort(R.string.network_error)
            }
        }
    }
}