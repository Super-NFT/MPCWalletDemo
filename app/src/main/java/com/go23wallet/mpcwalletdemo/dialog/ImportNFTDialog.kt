package com.go23wallet.mpcwalletdemo.dialog

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.Go23WalletManage
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.coins.app.util.KeyboardUtils
import com.go23.bean.nft.NftResponse
import com.go23.callback.BaseCallBack
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogImportNftLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.CustomToast
import com.go23wallet.mpcwalletdemo.utils.KeyboardStatusWatcher
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager
import com.google.zxing.activity.CaptureActivity

class ImportNFTDialog :
    BaseDialogFragment<DialogImportNftLayoutBinding>() {

    override val layoutId: Int = R.layout.dialog_import_nft_layout

    override fun initViews(v: View?) {
        dialog?.window?.let {
            KeyboardStatusWatcher.setKeyBoardListener(it) {
                viewBinding.tvImport.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomMargin = this@setKeyBoardListener
                }
            }
        }
        viewBinding.etNtfAddress.requestFocus()
        viewBinding.etNtfAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewBinding.ivNftClear.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                viewBinding.tvImport.isEnabled =
                    (viewBinding.etNtfAddress.text?.length ?: 0) >= 30
            }
        })

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val data = it.data?.getStringExtra("result") ?: ""
            val content = if (data.contains(":")) {
                if (data.contains("@")) {
                    data.split(":")[1].split("@")[0]
                } else {
                    data.split(":")[1]
                }
            } else {
                if (data.contains("@")) {
                    data.split("@")[0]
                } else {
                    data
                }
            }
            if (!content.isNullOrEmpty()) {
                viewBinding.etNtfAddress.setText(content)
            }
        }

        viewBinding.ivScanCode.setOnClickListener {
            PermissionUtils.permission(PermissionConstants.CAMERA)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        try {
                            registerResult.launch(Intent(context, CaptureActivity::class.java))
                        } catch (ignored: Exception) {
                            CustomToast.showShort(R.string.lite_request_camera_permission_fail)
                        }
                    }

                    override fun onDenied() {
                        CustomToast.showShort(R.string.lite_request_camera_permission_fail)
                    }
                }).request()
        }

        viewBinding.ivNftClear.setOnClickListener {
            viewBinding.etNtfAddress.setText("")
            viewBinding.ivNftClear.visibility = View.GONE
        }

        viewBinding.tvImport.setOnClickListener {
            var s = viewBinding.etNtfAddress.text.toString()
            Go23WalletManage.getInstance().addNft(
                UserWalletInfoManager.getUserWalletInfo().walletInfo.wallet_address,
                UserWalletInfoManager.getUserWalletInfo().userChain.chain_id,
                s,
                object : BaseCallBack<NftResponse> {
                    override fun success(data: NftResponse?) {
                        data?.let {
                            if (it.code == 0) {
                                CustomToast.showShort(R.string.lite_add_success)
                                UpdateDataLiveData.setUpdateType(2)
                                dismissAllowingStateLoss()
                            } else {
                                CustomToast.showShort(it.message)
                            }
                        } ?: kotlin.run {
                            CustomToast.showShort(R.string.lite_add_fail)
                        }
                    }

                    override fun failed() {
                        CustomToast.showShort(R.string.lite_add_fail)
                    }
                })
        }

        viewBinding.root.setOnClickListener {
            KeyboardUtils.hideKeyboard(viewBinding.root)
        }

    }

    override fun onStop() {
        super.onStop()
        KeyboardUtils.hideKeyboard(viewBinding.root)
        viewBinding.tvImport.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomMargin = 0
        }
    }
}