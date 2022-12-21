package com.go23wallet.mpcwallet.base

import androidx.viewbinding.ViewBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import kotlin.jvm.JvmOverloads
import com.blankj.utilcode.util.LogUtils
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * 基础Activity类
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected var mActivity: BaseActivity<*>? = null
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        if (layoutRes != 0) {
            val type = javaClass.genericSuperclass as ParameterizedType
            val cls = type.actualTypeArguments[0] as Class<*>
            try {
                val inflate = cls.getDeclaredMethod("inflate", LayoutInflater::class.java)
                binding = inflate.invoke(null, layoutInflater) as T
                setContentView(binding!!.root)
                initViews(savedInstanceState)
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取当前布局文件资源id
     *
     * @return layoutResId
     */
    protected abstract val layoutRes: Int

    /**
     * 设置view相关事件
     */
    protected abstract fun initViews(savedInstanceState: Bundle?)
    private var mProgressDialog: AnimateProgressDialog? = null

    private fun getProgressDialog(msg: String?): AnimateProgressDialog? {
        if (mProgressDialog == null) {
            mProgressDialog = AnimateProgressDialog()
        } else {
            if (mProgressDialog?.isAdded == true) {
                mProgressDialog?.dismissAllowingStateLoss()
            }
        }
        mProgressDialog?.setMessage(msg)
        return mProgressDialog
    }

    /**
     * 显示进度提示
     */
    @JvmOverloads
    fun showProgress(msg: String = "") {
        if (msg.contains("null")) {
            return
        }
        runOnUiThread {
            try {
                getProgressDialog(msg)?.show(supportFragmentManager, "progressDialog")
            } catch (e: Exception) {
                LogUtils.e("showProgress: ", e)
            }
        }
    }

    fun dismissProgress() {
        runOnUiThread {
            try {
                if (mProgressDialog?.isAdded == true) {
                    mProgressDialog?.dismissAllowingStateLoss()
                }
            } catch (e: Exception) {
                LogUtils.e("dismissProgress: ", e)
            }
        }
    }
}