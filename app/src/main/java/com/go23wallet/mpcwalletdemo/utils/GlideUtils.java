package com.go23wallet.mpcwalletdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.go23wallet.mpcwalletdemo.R;

import java.io.File;

import androidx.annotation.Nullable;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class GlideUtils {

    /**加载本地图片*/
    public static void loadFile(Context context, String path, ImageView imageView) {
        try {
            File filef = new File(path);
            if (!filef.exists()) {
                return;
            }
            Glide.with(context).load(path).into(imageView);
        } catch (Exception e) {
        }
    }

    //加载网络图片
    public static void loadImg(Context context, String url, ImageView view) {
        //DiskCacheStrategy.DATA 只缓存原始图片，会导致个别转换过后的图片显示失败，如后台合成的群头像图片
        //DiskCacheStrategy.RESOURCE 只缓存转换过后的图片
        //DiskCacheStrategy.ALL 既缓存原始图片，也缓存转换过后的图片；对于远程图片，缓存 DATA和 RESOURCE；对于本地图片，只缓存 RESOURCE
        //DiskCacheStrategy.NONE：不缓存任何内容；
        //DiskCacheStrategy.AUTOMATIC：默认策略，尝试对本地和远程图片使用最佳的策略。
        //当下载网络图片时，使用DATA(原因很简单，对本地图片的处理可比网络要容易得多)；对于本地图片，使用RESOURCE。

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.normal_default_img)
                .error(R.drawable.normal_default_img);

        Glide.with(context).load(url).apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//只缓存转换过后的图片
                .into(view);
//                .listener(new RequestListener() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
//                Log.e("测试...", "onException: " + e.toString()+" model:"+model+" isFirstResource: "+isFirstResource);
//                view.setImageResource(R.drawable.ic_launcher);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
//                Log.d("测试...",  "model:"+model+" isFirstResource: "+isFirstResource);
//                return false;
//            }})
    }

    //加载网络图片
    public static void loadImg(Context context, String url, ImageView view, int errorImg) {
        //DiskCacheStrategy.DATA 只缓存原始图片，会导致个别转换过后的图片显示失败，如后台合成的群头像图片
        //DiskCacheStrategy.RESOURCE 只缓存转换过后的图片
        //DiskCacheStrategy.ALL 既缓存原始图片，也缓存转换过后的图片；对于远程图片，缓存 DATA和 RESOURCE；对于本地图片，只缓存 RESOURCE
        //DiskCacheStrategy.NONE：不缓存任何内容；
        //DiskCacheStrategy.AUTOMATIC：默认策略，尝试对本地和远程图片使用最佳的策略。
        //当下载网络图片时，使用DATA(原因很简单，对本地图片的处理可比网络要容易得多)；对于本地图片，使用RESOURCE。

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(errorImg)
                .error(errorImg);

        Glide.with(context).load(url).apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//只缓存转换过后的图片
                .into(view);
    }

    public static void loadCenterCorpRoundImg(Context context, String url, ImageView view,int round) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.normal_default_img)
                .error(R.drawable.normal_default_img);

        Glide.with(context).load(url).apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//只缓存转换过后的图片
                .transform(new GlideRoundedCornersTransform(context, round, GlideRoundedCornersTransform.CornerType.ALL))

                .into(view);
    }

    //加载本地图片
    public static void loadImg(Context context, int res, ImageView view) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.load_error_img)
                .error(R.drawable.load_error_img);

        Glide.with(context).load(res).apply(requestOptions).into(view);
    }

    /**
     * 加载圆角  有错误页
     *
     * @param url
     * @param view
     * @param r
     */
    public static void loadRound(Context context, String url, ImageView view, int r) {
        int radius = (int) (Resources.getSystem().getDisplayMetrics().density * r);
        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .transform(new GlideRoundTransform(view.getContext(), r))
                .transform(new RoundedCorners(radius))
                .placeholder(R.drawable.load_error_img)
                .error(R.drawable.load_error_img);

        Glide.with(context).load(url).apply(requestOptions)
                .into(view);
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
    public static void loadRound(Context context, String url, ImageView view, int r, int errorImg) {
        int radius = (int) (Resources.getSystem().getDisplayMetrics().density * r);
        RequestOptions requestOptions = new RequestOptions()
                .dontAnimate()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .transform(new GlideRoundTransform(view.getContext(), r))
                .transform(new RoundedCorners(radius))
                .placeholder(errorImg)
                .error(errorImg);

        Glide.with(context).load(url).apply(requestOptions)
                .into(view);
    }

    /**
     * 加载的为bitmap
     *
     * @param context
     * @param url
     * @param view
     * @param r
     */
    public static void loadRound(Context context, Bitmap url, ImageView view, int r) {
        int radius = (int) (Resources.getSystem().getDisplayMetrics().density * r);
        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .transform(new GlideRoundTransform(view.getContext(), r))
                .transform(new RoundedCorners(radius))
                .placeholder(R.drawable.load_error_img)
                .error(R.drawable.load_error_img);

        Glide.with(context).load(url).apply(requestOptions)
                .into(view);
    }

    /**
     * 加载圆角  wu错误页
     *
     * @param url
     * @param view
     * @param r
     */
    public static void loadNoERound(Context context, String url, ImageView view, int r) {
        int radius = (int) (Resources.getSystem().getDisplayMetrics().density * r);
        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .transform(new GlideRoundTransform(view.getContext(), r));
                .transform(new RoundedCorners(radius));

        Glide.with(context).load(url).apply(requestOptions)
                .into(view);
    }


    //高斯模糊
    public static void loadBlurry(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//缓存
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(50, 2)))
                .transition(DrawableTransitionOptions.withCrossFade())//淡入淡出
                .into(view);
    }

    public static void loadBlurry(Context context, String url, ImageView view, int value) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//缓存
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(value, value)))
                .transition(DrawableTransitionOptions.withCrossFade())//淡入淡出
                .into(view);
    }


    /**
     * 无错误页
     *
     * @param url
     * @param view
     */
    public static void loadImgNoEmpty(Context context, String url, ImageView view) {
        if (view != null) {
            RequestOptions requestOptions = new RequestOptions();
            Glide.with(context).load(url).apply(requestOptions).into(view);
        }
    }


    /**
     * 视频封面专用
     *
     * @param context
     * @param url
     * @param view
     */
    public static void loadVideoThumb(Context context, String url, ImageView view) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(false);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.format(DecodeFormat.PREFER_RGB_565);
        Glide.with(context).load(url).apply(requestOptions).into(view);
    }


    /**
     * 视频封面专用  高斯模糊
     *
     * @param context
     * @param url
     * @param view
     */
    public static void loadVideoBlurry(Context context, String url, ImageView view) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(false);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 15)))
                .into(view);
    }

    /**
     * gif图片
     */
    public static void loadGifResource(Activity context, int id, ImageView imageView) {
        if (context == null || context.isFinishing()) {
            return;
        }
        Glide.with(context).asGif().load(id).diskCacheStrategy(DiskCacheStrategy.RESOURCE).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }


}
