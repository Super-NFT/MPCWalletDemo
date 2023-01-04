package com.go23wallet.mpcwalletdemo.utils

import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

object KeyboardStatusWatcher {

    fun setKeyBoardListener(window: Window, callback: Int.() -> Unit = {}) {
        ViewCompat.setWindowInsetsAnimationCallback(
            window.decorView,
            object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat {

                    val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                    val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

                    //当前是否展示
                    callback.invoke(keyboardHeight)
                    return insets
                }
            })

        ViewCompat.getWindowInsetsController(window.findViewById(android.R.id.content))?.apply {

            show(WindowInsetsCompat.Type.ime())

        }
    }
}