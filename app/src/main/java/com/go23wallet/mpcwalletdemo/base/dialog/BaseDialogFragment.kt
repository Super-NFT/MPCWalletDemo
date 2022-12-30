package com.go23wallet.mpcwalletdemo.base.dialog

import android.app.Dialog
import androidx.viewbinding.ViewBinding
import android.os.Bundle
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

abstract class BaseDialogFragment<T : ViewBinding> : DialogFragment() {
    private var DEFAULT_WIDTH = WindowManager.LayoutParams.MATCH_PARENT //宽
    private var DEFAULT_HEIGHT = WindowManager.LayoutParams.WRAP_CONTENT //高
    private var DEFAULT_GRAVITY = Gravity.BOTTOM //位置
    lateinit var viewBinding: T
    private var mCancelable = false //默认不可取消
    private var mCanceledOnTouchOutside = false //默认点击外部不可取消
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(layoutId, container, false)
        val type = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        try {
            val inflate = cls.getDeclaredMethod("inflate", LayoutInflater::class.java)
            viewBinding = inflate.invoke(null, layoutInflater) as T
            initViews(mView)
            return viewBinding?.root ?: mView
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        initViews(mView)
        return mView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog = super.onCreateDialog(savedInstanceState)
        if (null != mDialog) { //初始化
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
            mDialog.setCancelable(mCancelable)
            val window = mDialog.window
            if (null != window) {
                window.decorView.setPadding(0, 0, 0, 0)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val lp = window.attributes
                lp.width = DEFAULT_WIDTH
                lp.height = DEFAULT_HEIGHT
                lp.gravity = DEFAULT_GRAVITY
                window.attributes = lp
            }
            mDialog.setOnKeyListener { dialog, keyCode, event -> !mCancelable }
        }
        return mDialog
    }

    /**
     * 设置位置
     *
     * @param gravity
     */
    fun setGravity(gravity: Int) {
        DEFAULT_GRAVITY = gravity
    }

    /**
     * 设置宽
     *
     * @param width
     */
    fun setWidth(width: Int) {
        DEFAULT_WIDTH = width
    }

    /**
     * 设置高
     *
     * @param height
     */
    fun setHeight(height: Int) {
        DEFAULT_HEIGHT = height
    }

    /**
     * 设置点击返回按钮是否可取消
     *
     * @param cancelable
     */
    override fun setCancelable(cancelable: Boolean) {
        mCancelable = cancelable
    }

    /**
     * 设置点击外部是否可取消
     *
     * @param canceledOnTouchOutside
     */
    fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean) {
        mCanceledOnTouchOutside = canceledOnTouchOutside
    }

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract val layoutId: Int

    /**
     * 初始化Views
     *
     * @param v
     */
    protected abstract fun initViews(v: View?)

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded) {
            super.show(manager, tag)
//            dismissAllowingStateLoss()
        }

    }

    fun isLateinited(): Boolean {
        return this::viewBinding.isInitialized
    }
}