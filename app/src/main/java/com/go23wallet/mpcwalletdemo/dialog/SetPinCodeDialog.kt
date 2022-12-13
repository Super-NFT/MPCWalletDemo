package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSetPinCodeLayoutBinding

class SetPinCodeDialog(mContext: Context) : BaseDialogFragment<DialogSetPinCodeLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_set_pin_code_layout

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    private var setPinCode = ""
    private var confirmPinCode = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
    }
    override fun initViews(v: View?) {
        viewBinding.ivBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.etSet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                setPinCode = s.toString()
            }

        })
        viewBinding.etConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                confirmPinCode = s.toString()
            }

        })

        viewBinding.tvConfirm.setOnClickListener {
            if (setPinCode == confirmPinCode && setPinCode.length == 8) {
                viewBinding.confirmGroup.visibility = View.GONE
                viewBinding.tvSuccess.visibility = View.VISIBLE
                mHandler.postDelayed({
                    dismissAllowingStateLoss()
                }, 1000)
            } else {
                Toast.makeText(context, R.string.confirm_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}