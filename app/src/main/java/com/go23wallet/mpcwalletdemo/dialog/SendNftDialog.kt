package com.go23wallet.mpcwalletdemo.dialog

import android.content.Context
import android.view.*
import com.blankj.utilcode.util.ScreenUtils
import com.go23wallet.mpcwalletdemo.R
import com.go23wallet.mpcwalletdemo.base.dialog.BaseDialogFragment
import com.go23wallet.mpcwalletdemo.databinding.DialogSendNftLayoutBinding
import com.go23wallet.mpcwalletdemo.utils.GlideUtils


class SendNftDialog(private val mContext: Context, private val mData: String) :
    BaseDialogFragment<DialogSendNftLayoutBinding>() {

    private val transactionResultDialog: TransactionResultDialog by lazy {
        TransactionResultDialog(mContext)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHeight((ScreenUtils.getScreenHeight() * 0.8).toInt())
        setGravity(Gravity.BOTTOM)
    }

    override val layoutId: Int = R.layout.dialog_send_nft_layout

    override fun initViews(v: View?) {
        viewBinding.tvTitle.text = mData
        GlideUtils.loadImg(context, mData, viewBinding.ivNftPro)
        GlideUtils.loadImg(context, mData, viewBinding.ivNftAvatar)

        viewBinding.tvNftProNo.text = mData
        viewBinding.tvNftPro.text = mData
        viewBinding.tvNftNo.text = mData
        viewBinding.tvNftTokenId.text = mData
        viewBinding.tvContactContent.text = mData
        viewBinding.tvFromContent.text = mData
        viewBinding.tvToContent.text = mData
        viewBinding.tvTokenIdContent.text = mData
        viewBinding.tvValueContent.text = mData
        viewBinding.tvGasContent.text = mData
        viewBinding.tvGasValue.text = mData

        viewBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        viewBinding.tvConfirm.setOnClickListener {
            transactionResultDialog.show(parentFragmentManager, "transactionResultDialog")
        }

    }
}