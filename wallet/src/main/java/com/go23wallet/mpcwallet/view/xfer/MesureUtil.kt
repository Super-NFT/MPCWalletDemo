package com.go23wallet.mpcwallet.view.xfer

import android.content.Context
import com.go23wallet.mpcwallet.view.xfer.MesureUtil
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import com.go23wallet.mpcwallet.R
import kotlin.jvm.JvmOverloads
import android.view.ViewGroup

class MesureUtil {
    private var mBaseType = BASE_TYPE_WIDTH
    private var mRatio = 0f
    fun obtainStyledAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val b = context.obtainStyledAttributes(attrs, R.styleable.View, defStyleAttr, 0)
        mRatio = b.getFloat(R.styleable.View_atsize_ratio, 0f)
        mBaseType = b.getInteger(R.styleable.View_atsize_base_type, BASE_TYPE_WIDTH)
        b.recycle()
    }

    private var mWH: IntArray? = null

    @JvmOverloads
    fun onMeasure(
        lp: ViewGroup.LayoutParams,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
        maxW: Int = Int.MAX_VALUE,
        maxH: Int = Int.MAX_VALUE
    ): IntArray {
        if (mWH == null) {
            mWH = intArrayOf(widthMeasureSpec, heightMeasureSpec)
        }
        if (mRatio > 0) {
            var ratioW = lp.width
            var ratioH = lp.height
            //参考边为：宽
            if (mBaseType == BASE_TYPE_WIDTH && ratioH <= 0) {
                if (ratioW <= 0) {
                    ratioW = View.MeasureSpec.getSize(widthMeasureSpec)
                }
                if (ratioW > maxW) {
                    ratioW = maxW
                }
                ratioH = (ratioW * mRatio).toInt() //根据宽来计算高
                //参考边为：高
            } else if (mBaseType == BASE_TYPE_HEIGHT && ratioW <= 0) {
                if (ratioH <= 0) {
                    ratioH = View.MeasureSpec.getSize(heightMeasureSpec)
                }
                if (ratioH > maxH) {
                    ratioH = maxH
                }
                ratioW = (ratioH * mRatio).toInt() //根据高来计算宽
            }
            //丢给父类处理
            mWH!![0] = View.MeasureSpec.makeMeasureSpec(ratioW, View.MeasureSpec.EXACTLY)
            mWH!![1] = View.MeasureSpec.makeMeasureSpec(ratioH, View.MeasureSpec.EXACTLY)
        } else {
            mWH!![0] = widthMeasureSpec
            mWH!![1] = heightMeasureSpec
        }
        return mWH as IntArray
    }

    companion object {
        private const val BASE_TYPE_WIDTH = 0
        private const val BASE_TYPE_HEIGHT = 1
    }
}