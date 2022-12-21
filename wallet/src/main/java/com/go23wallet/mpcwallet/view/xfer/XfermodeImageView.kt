package com.go23wallet.mpcwallet.view.xfer

import android.content.Context
import kotlin.jvm.JvmOverloads
import com.go23wallet.mpcwallet.view.xfer.AutoSizeImageView
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import com.go23wallet.mpcwallet.R
import com.go23wallet.mpcwallet.view.xfer.XfermodeImageView
import android.util.AttributeSet
import java.lang.Exception
import java.lang.ref.WeakReference

/**
 * 自定义形状的imageview，通过background设置不同形状
 */
class XfermodeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AutoSizeImageView(context, attrs) {
    protected var mPaint: Paint
    protected var mXfermode: Xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    /**
     * 边框宽度
     */
    protected var mBorderWidth: Float

    /**
     * 边框颜色
     */
    private var mBorderColor: Int
    protected var mWeakBitmap: WeakReference<Bitmap?>? = null
    private var mBgSrcDrawable: Drawable?
    private var mNeedScaleImg = true
    private var mNeedDrawCache = true
    private var needXfer = true
    fun setNeedXfer(needXfer: Boolean) {
        this.needXfer = needXfer
        //        invalidate();
    }

    fun setNeedDrawCache(needDrawCache: Boolean) {
        mNeedDrawCache = needDrawCache
    }

    fun setBorderWidth(borderWidth: Float) {
        mBorderWidth = borderWidth
    }

    fun setBorderColor(borderColor: Int) {
        mBorderColor = borderColor
    }

    private var mDrawCanvas: Canvas? = null

    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        val a = context.obtainStyledAttributes(attrs, R.styleable.XfermodeImageView, defStyle, 0)
        mBorderWidth =
            a.getDimensionPixelSize(R.styleable.XfermodeImageView_xiv_border_width, 0).toFloat()
        mBorderColor = a.getColor(R.styleable.XfermodeImageView_xiv_border_color, Color.LTGRAY)
        mBgSrcDrawable = a.getDrawable(R.styleable.XfermodeImageView_xiv_bg_src)
        mNeedScaleImg = a.getBoolean(R.styleable.XfermodeImageView_xiv_need_scale, true)
        mNeedDrawCache = a.getBoolean(R.styleable.XfermodeImageView_xiv_draw_cache, true)
        val xfermode = a.getInteger(R.styleable.XfermodeImageView_xiv_xfer_mode, MODE_IN)
        if (xfermode == MODE_IN) {
            mXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        } else if (xfermode == MODE_OUT) {
            mXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        if (needXfer) {
            // 在缓存中取出bitmap
            var bitmap = mWeakBitmap?.get()
            try {
                if (!mNeedDrawCache || null == bitmap || bitmap.isRecycled) {
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    if (mDrawCanvas == null) {
                        mDrawCanvas = Canvas()
                    }
                    mDrawCanvas?.setBitmap(bitmap)
                    var borderBM: Bitmap? = borderBitmap
                    borderBM?.let { mDrawCanvas?.drawBitmap(it, 0f, 0f, null) }
                    var srcBM = srcBitmap
                    srcBM?.let { mDrawCanvas?.drawBitmap(it, mBorderWidth, mBorderWidth, null) }
                    mPaint.xfermode = null
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint)
                    // bitmap缓存起来，避免每次调用onDraw，分配内存
                    // 使用此缓存会导致gif失效
                    mWeakBitmap = WeakReference(bitmap)
                    srcBM?.recycle()
                    srcBM = null
                    borderBM?.recycle()
                    borderBM = null
                } else if (bitmap != null) {
                    mPaint.xfermode = null
                    bitmap = resizeBitmap(bitmap)
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint)
                }
            } catch (e: Exception) {
//            LogDebug.e(e);
            } catch (oe: OutOfMemoryError) {
            }
        } else {
            super.onDraw(canvas)
        }
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        var bitmap = bitmap
        val nowWidth = width
        val nowHeight = height
        val orWidth = bitmap.width
        val orHeight = bitmap.height
        if (nowWidth != orWidth) {
            val scalex = nowWidth.toFloat() / orWidth.toFloat()
            val scaley = nowHeight.toFloat() / orHeight.toFloat()
            val matrix = Matrix()
            matrix.postScale(scalex, scaley) //长和宽放大缩小的比例
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, orWidth, orHeight, matrix, true)
        }
        return bitmap
    }// 按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；

    // 根据缩放比例，设置bounds，相当于缩放图片了
    // Draw Bitmap.
    // 绘制形状
    // 获取drawable的宽和高
    private val srcBitmap: Bitmap?
        private get() {
            val drawable = drawable
            if (drawable != null) {
                // 获取drawable的宽和高
                val dWidth = drawable.intrinsicWidth
                val dHeight = drawable.intrinsicHeight
                val rWidth = (width - mBorderWidth * 2).toInt()
                val rHeight = (height - mBorderWidth * 2).toInt()

                // 创建bitmap
                val bitmap = Bitmap.createBitmap(rWidth, rHeight, Bitmap.Config.ARGB_8888)
                var scale = 1.0f
                // 创建画布
                val drawCanvas = Canvas(bitmap)
                if (mNeedScaleImg) {
                    // 按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；
                    scale = Math.max(rWidth * 1.0f / dWidth, rHeight * 1.0f / dHeight)
                    // 根据缩放比例，设置bounds，相当于缩放图片了
                    drawable.setBounds(
                        (rWidth - (scale * dWidth).toInt()) / 2,
                        (rHeight - (scale * dHeight).toInt()) / 2,
                        (rWidth + (scale * dWidth).toInt()) / 2,
                        (rHeight + (scale * dHeight).toInt()) / 2
                    )
                }
                drawable.draw(drawCanvas)
                var maskbm: Bitmap? = drawable2Bitmap(background, mBorderWidth.toInt())
                // Draw Bitmap.
                mPaint.reset()
                mPaint.isFilterBitmap = false
                mPaint.xfermode = mXfermode
                // 绘制形状
                maskbm?.let { drawCanvas.drawBitmap(it, 0f, 0f, mPaint) }
                mPaint.xfermode = null
                maskbm?.recycle()
                maskbm = null
                return bitmap
            }
            return null
        }

    // 创建bitmap
    private val borderBitmap: Bitmap
        private get() {
            // 创建bitmap
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            // 创建画布
            val drawCanvas = Canvas(bitmap)
            drawCanvas.drawColor(mBorderColor)
            var maskborbm: Bitmap? = drawable2Bitmap(background, 0)
            // Draw Bitmap.
            mPaint.reset()
            mPaint.isFilterBitmap = false
            mPaint.xfermode = mXfermode
            // 绘制形状
            maskborbm?.let { drawCanvas.drawBitmap(it, 0f, 0f, mPaint) }
            mPaint.xfermode = null
            maskborbm?.recycle()
            maskborbm = null
            return bitmap
        }

    fun setBgSrcDrawable(drawable: Drawable?) {
        mBgSrcDrawable = drawable
    }

    override fun getBackground(): Drawable {
        if (mBgSrcDrawable != null) {
            return mBgSrcDrawable as Drawable
        }
        if (super.getBackground() == null) {
            setBackgroundResource(R.drawable.default_shape_round)
        }
        return super.getBackground()
    }

    /**
     * 绘制形状
     *
     * @return
     */
    private val bitmap: Bitmap
        private get() {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.BLACK
            canvas.drawBitmap(drawable2Bitmap(background, 0), 0f, 0f, paint)
            return bitmap
        }

    /**
     * Drawable 转 bitmap
     *
     * @param drawable
     * @return
     */
    private fun drawable2Bitmap(drawable: Drawable, cut: Int): Bitmap {
        val dWidth = width
        val dHeight = height
        //        int dWidth = drawable.getIntrinsicWidth();
//        int dHeight = drawable.getIntrinsicHeight();
        val rWidth = (width - cut * 2)
        val rHeight = (height - cut * 2)
        var scale = 1.0f
        val bitmap = Bitmap.createBitmap(
            rWidth, rHeight,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        // 按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；
        scale = Math.max(rWidth * 1.0f / dWidth, rHeight * 1.0f / dHeight)
        // 根据缩放比例，设置bounds，相当于缩放图片了
        drawable.setBounds(
            (rWidth - (scale * dWidth).toInt()) / 2,
            (rHeight - (scale * dHeight).toInt()) / 2,
            (rWidth + (scale * dWidth).toInt()) / 2,
            (rHeight + (scale * dHeight).toInt()) / 2
        )
        drawable.draw(canvas)
        drawable.setBounds(0, 0, width, height)
        return bitmap
    }

    override fun setImageBitmap(bm: Bitmap) {
        mWeakBitmap = null
        super.setImageBitmap(bm)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        mWeakBitmap = null
        super.setImageDrawable(drawable)
    }

    override fun setImageResource(resId: Int) {
        mWeakBitmap = null
        super.setImageResource(resId)
    }

    override fun setImageURI(uri: Uri?) {
        mWeakBitmap = null
        super.setImageURI(uri)
    }

    companion object {
        private const val MODE_IN = 0
        private const val MODE_OUT = 1
    }
}