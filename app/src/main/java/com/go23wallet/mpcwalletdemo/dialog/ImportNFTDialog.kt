package com.go23wallet.mpcwalletdemo.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogImportNftLayoutBinding

class ImportNFTDialog :
    BaseDialogFragment<DialogImportNftLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_import_nft_layout

    override fun initViews(v: View?) {

        viewBinding.etNtfAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewBinding.ivNftClear.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                viewBinding.tvImport.isEnabled =
                    viewBinding.etNtfAddress.text?.isNotEmpty() == true && viewBinding.etTokenAddress.text?.isNotEmpty() == true
            }
        })

        viewBinding.etTokenAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewBinding.ivTokenClear.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                viewBinding.tvImport.isEnabled =
                    viewBinding.etNtfAddress.text?.isNotEmpty() == true && viewBinding.etTokenAddress.text?.isNotEmpty() == true
            }
        })

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.ivNftClear.setOnClickListener {
            viewBinding.etNtfAddress.setText("")
            viewBinding.ivNftClear.visibility = View.GONE
        }

        viewBinding.ivTokenClear.setOnClickListener {
            viewBinding.etTokenAddress.setText("")
            viewBinding.ivTokenClear.visibility = View.GONE
        }

        viewBinding.tvImport.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}