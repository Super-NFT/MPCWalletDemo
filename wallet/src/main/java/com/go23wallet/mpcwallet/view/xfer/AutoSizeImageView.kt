package com.go23wallet.mpcwallet.view.xfer

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.go23wallet.mpcwallet.view.xfer.MesureUtil

open class AutoSizeImageView : AppCompatImageView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        obtainStyledAttributes(context, attrs, 0, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        obtainStyledAttributes(context, attrs, defStyleAttr, 0)
    }

    private var mesureUtil: MesureUtil? = null
    fun obtainStyledAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        mesureUtil = MesureUtil()
        mesureUtil?.obtainStyledAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mesure = mesureUtil!!.onMeasure(
            layoutParams,
            widthMeasureSpec,
            heightMeasureSpec,
            maxWidth,
            maxHeight
        )
        super.onMeasure(mesure[0], mesure[1])
    }
}