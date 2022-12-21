package com.go23wallet.mpcwallet.dialog

import android.content.Context
import android.content.Intent
import android.view.*
import com.go23wallet.mpcwallet.R
import com.go23wallet.mpcwallet.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwallet.databinding.DialogImportAssetLayoutBinding
import com.go23wallet.mpcwallet.wallet.AddATokenActivity

class ImportAssetDialog(private val mContext: Context) :
    BaseDialogFragment<DialogImportAssetLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_import_asset_layout

    private val importNFTDialog: ImportNFTDialog by lazy {
        ImportNFTDialog()
    }

    override fun initViews(v: View?) {
        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvToken.setOnClickListener {
            startActivity(Intent(mContext, AddATokenActivity::class.java))
            dismissAllowingStateLoss()
        }

        viewBinding.tvNfts.setOnClickListener {
            importNFTDialog.show(parentFragmentManager, "importNFTDialog")
            dismissAllowingStateLoss()
        }
    }
}