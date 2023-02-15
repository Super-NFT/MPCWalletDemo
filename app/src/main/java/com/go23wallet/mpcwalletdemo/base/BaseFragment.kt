package com.go23wallet.mpcwalletdemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


abstract class BaseFragment<T : ViewBinding> : Fragment() {
    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*getClass 获取该类的类类型(BaseFragment子类的类型)
       * getGenericSuperclass() 获得该类带有泛型的父类(在子类中确定的BaseFragment的类型)
       * Type是 Java中所有类型的公共高级接口。包括原始类型、参数化类型、数组类型、类型变量和基本类型
       * ParameterizedType参数化类型，即泛型
       * getActualTypeArguments获取参数化类型的数组，泛型可能有多个
        * */
        try {
            val superclass: Type = javaClass.genericSuperclass
            //获得父类的泛型参数的实际类型
            val aClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<*>
            //获取inflate方法 传入相应的参数
            val method: Method = aClass.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.javaPrimitiveType
            )
            //执行inflate方法
            binding = method.invoke(null, layoutInflater, container, false) as T
            initViews()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return binding.root
    }

    protected abstract fun initViews()

    open fun updateOffset(offset: Int) {}

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
                getProgressDialog(msg)?.show(parentFragmentManager, "progressDialog")
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

