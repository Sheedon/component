package org.sheedon.tool.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageView


/**
 * View工具类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2023/3/4 14:38
 */
object ViewUtils {

    fun expandAndCollapseToUpdateImage(
        view: ImageView,
        isShow: Boolean, expandRes: Drawable, collapseRes: Drawable
    ) {
        view.setImageDrawable(if (!isShow) expandRes else collapseRes)
    }


    /**
     * 隐藏状态栏
     */
    @SuppressLint("ClickableViewAccessibility")
    @JvmStatic
    fun hideStatusBar(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            context.window.insetsController?.also {
                // 隐藏状态栏
                it.hide(WindowInsets.Type.statusBars())

                // 隐藏底部导航栏
                it.hide(WindowInsets.Type.navigationBars())

                // 如果需要同时隐藏导航栏上的导航手势提示条（如Android 11的滑动手势提示条），可以添加以下代码
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // 隐藏状态栏
            val decorView = context.window.decorView
            var uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions

            // 隐藏底部导航栏（虚拟导航栏）
            uiOptions =
                uiOptions or (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            decorView.systemUiVisibility = uiOptions
        }

    }
}