package com.go23wallet.mpcwallet.utils

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.go23wallet.mpcwallet.R
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.go23wallet.mpcwallet.utils.GlideRoundedCornersTransform
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.graphics.Bitmap
import jp.wasabeef.glide.transformations.BlurTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.DecodeFormat
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import java.io.File
import java.lang.Exception

object GlideUtils {
    /**加载本地图片 */
    fun loadFile(context: Context, path: String?, imageView: ImageView) {
        try {
            val filef = File(path)
            if (!filef.exists()) {
                return
            }
            Glide.with(context).load(path).into(imageView)
        } catch (e: Exception) {
        }
    }

    //加载网络图片
    fun loadImg(context: Context, url: String?, view: ImageView) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.normal_default_img)
            .error(R.drawable.normal_default_img)
        Glide.with(context).load(url).apply(requestOptions)
            .diskCacheStrategy(DiskCacheStrategy.ALL) //只缓存转换过后的图片
            .into(view)
    }

    //加载网络图片
    fun loadImg(context: Context, url: String?, view: ImageView, errorImg: Int) {
        val requestOptions = RequestOptions()
            .placeholder(errorImg)
            .error(errorImg)
        Glide.with(context).load(url).apply(requestOptions)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }

    fun loadCenterCorpRoundImg(context: Context, url: String?, view: ImageView, round: Int) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.normal_default_img)
            .error(R.drawable.normal_default_img)
        Glide.with(context).load(url).apply(requestOptions)
            .diskCacheStrategy(DiskCacheStrategy.ALL) //只缓存转换过后的图片
            .transform(
                com.go23wallet.mpcwallet.utils.GlideRoundedCornersTransform(
                    context,
                    round.toFloat(),
                    com.go23wallet.mpcwallet.utils.GlideRoundedCornersTransform.CornerType.ALL
                )
            )
            .into(view)
    }

    //加载本地图片
    fun loadImg(context: Context, res: Int, view: ImageView) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.load_error_img)
            .error(R.drawable.load_error_img)
        Glide.with(context).load(res).apply(requestOptions).into(view)
    }

    /**
     * 加载圆角  有错误页
     *
     * @param url
     * @param view
     * @param r
     */
    fun loadRound(context: Context, url: String?, view: ImageView, r: Int) {
        val radius = (Resources.getSystem().displayMetrics.density * r).toInt()
        val requestOptions = RequestOptions()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.NONE) //                .transform(new GlideRoundTransform(view.getContext(), r))
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.load_error_img)
            .error(R.drawable.load_error_img)
        Glide.with(context).load(url).apply(requestOptions)
            .into(view)
    }

    /**
     * 圆角 自定义错误图
     *
     * @param context
     * @param url
     * @param view
     * @param r
     * @param errorImg
     */
    fun loadRound(context: Context, url: String?, view: ImageView, r: Int, errorImg: Int) {
        val radius = (Resources.getSystem().displayMetrics.density * r).toInt()
        val requestOptions = RequestOptions()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //                .transform(new GlideRoundTransform(view.getContext(), r))
            .transform(RoundedCorners(radius))
            .placeholder(errorImg)
            .error(errorImg)
        Glide.with(context).load(url).apply(requestOptions)
            .into(view)
    }

    /**
     * 加载的为bitmap
     *
     * @param context
     * @param url
     * @param view
     * @param r
     */
    fun loadRound(context: Context, url: Bitmap, view: ImageView, r: Int) {
        val radius = (Resources.getSystem().displayMetrics.density * r).toInt()
        val requestOptions = RequestOptions()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.NONE) //                .transform(new GlideRoundTransform(view.getContext(), r))
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.load_error_img)
            .error(R.drawable.load_error_img)
        Glide.with(context).load(url).apply(requestOptions)
            .into(view)
    }

    /**
     * 加载圆角  wu错误页
     *
     * @param url
     * @param view
     * @param r
     */
    fun loadNoERound(context: Context, url: String?, view: ImageView, r: Int) {
        val radius = (Resources.getSystem().displayMetrics.density * r).toInt()
        val requestOptions = RequestOptions()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //                .transform(new GlideRoundTransform(view.getContext(), r));
            .transform(RoundedCorners(radius))
        Glide.with(context).load(url).apply(requestOptions)
            .into(view)
    }

    //高斯模糊
    fun loadBlurry(context: Context, url: String?, view: ImageView) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //缓存
            .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 2)))
            .transition(DrawableTransitionOptions.withCrossFade()) //淡入淡出
            .into(view)
    }

    fun loadBlurry(context: Context, url: String?, view: ImageView, value: Int) {
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) //缓存
            .apply(RequestOptions.bitmapTransform(BlurTransformation(value, value)))
            .transition(DrawableTransitionOptions.withCrossFade()) //淡入淡出
            .into(view)
    }

    /**
     * 无错误页
     *
     * @param url
     * @param view
     */
    fun loadImgNoEmpty(context: Context, url: String?, view: ImageView) {
        val requestOptions = RequestOptions()
        Glide.with(context).load(url).apply(requestOptions).into(view)
    }

    /**
     * 视频封面专用
     *
     * @param context
     * @param url
     * @param view
     */
    fun loadVideoThumb(context: Context, url: String?, view: ImageView) {
        val requestOptions = RequestOptions()
        requestOptions.skipMemoryCache(false)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.format(DecodeFormat.PREFER_RGB_565)
        Glide.with(context).load(url).apply(requestOptions).into(view)
    }

    /**
     * 视频封面专用  高斯模糊
     *
     * @param context
     * @param url
     * @param view
     */
    fun loadVideoBlurry(context: Context, url: String?, view: ImageView) {
        val requestOptions = RequestOptions()
        requestOptions.skipMemoryCache(false)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(context)
            .load(url)
            .apply(requestOptions)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 15)))
            .into(view)
    }

    /**
     * gif图片
     */
    fun loadGifResource(context: Activity, id: Int, imageView: ImageView) {
        if (context == null || context.isFinishing) {
            return
        }
        Glide.with(context).asGif().load(id).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .listener(object : RequestListener<GifDrawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).into(imageView)
    }
}