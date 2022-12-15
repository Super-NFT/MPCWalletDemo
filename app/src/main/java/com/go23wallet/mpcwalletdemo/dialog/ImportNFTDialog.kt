package com.go23wallet.mpcwalletdemo.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.blankj.utilcode.util.ToastUtils
import com.coins.app.BaseCallBack
import com.coins.app.Go23WalletManage
import com.coins.app.bean.nft.NftResponse
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogImportNftLayoutBinding
import com.go23wallet.mpcwalletdemo.livedata.UpdateDataLiveData
import com.go23wallet.mpcwalletdemo.utils.UserWalletInfoManager

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
                    (viewBinding.etNtfAddress.text?.length ?: 0) >= 30
            }
        })

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.ivNftClear.setOnClickListener {
            viewBinding.etNtfAddress.setText("")
            viewBinding.ivNftClear.visibility = View.GONE
        }

        viewBinding.tvImport.setOnClickListener {
            Go23WalletManage.getInstance().checkNft(
                viewBinding.etNtfAddress.text.toString(),
                UserWalletInfoManager.getUserWalletInfo().userChainId,
                object : BaseCallBack<NftResponse> {
                    override fun success(data: NftResponse?) {
                        data?.data?.let {
                            Go23WalletManage.getInstance().addNft(
                                it.nft_id,
                                UserWalletInfoManager.getUserWalletInfo().userChainId,
                                UserWalletInfoManager.getUserWalletInfo().userWalletId,
                                object : BaseCallBack<NftResponse> {
                                    override fun success(p0: NftResponse?) {
                                        UpdateDataLiveData.liveData.postValue(2)
                                        dismissAllowingStateLoss()
                                    }

                                    override fun failed() {
                                        ToastUtils.showShort("add fail")
                                    }

                                })
                        }
                    }

                    override fun failed() {
                        ToastUtils.showShort("add fail")
                    }
                })
        }
    }
}