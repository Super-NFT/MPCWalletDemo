package com.go23wallet.mpcwalletdemo.dialog

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSetUserLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.KeyboardStatusWatcher

class SetUserDialog(val activity: Activity) : BaseDialogFragment<DialogSetUserLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_set_user_layout

    var callback: String.() -> Unit = {}

    private var setEmail = ""
    private var verifyCode = ""

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

        viewBinding.tvConfirm.setOnClickListener {
            dismissAllowingStateLoss()
            callback.invoke(setEmail)
        }
    }
}