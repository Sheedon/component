package org.sheedon.common.handler

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.sheedon.common.app.BaseApplication

/**
 * 文件资源转化处理器
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 5:04 下午
 */
class ResConvertHandler private constructor() {
    private var application: Application? = null

    companion object {
        private val INSTANCE = ResConvertHandler()

        @JvmStatic
        fun getInstance() = INSTANCE

        /**
         * 文字资源转为String类型
         *
         * @param resId 文字资源
         * @return 文字
         */
        fun convertString(@StringRes resId: Int): String {
            return INSTANCE.convertString(resId)
        }

        /**
         * 颜色资源转为颜色数值
         *
         * @param resId 颜色资源
         * @return 颜色数值
         */
        fun convertColor(@ColorRes resId: Int): Int {
            return INSTANCE.convertColor(resId)
        }

        /**
         * 颜色资源转为颜色数值
         *
         * @param resId 颜色资源
         * @return 颜色数值
         */
        fun convertDrawable(@DrawableRes resId: Int): Drawable {
            return INSTANCE.convertDrawable(resId)
        }
    }

    /**
     * 外部主动附加替换 Application
     *
     * @param application Application
     */
    fun attachApplication(application: Application) {
        this.application = application
    }

    /**
     * 获取附加的Application，用于转化操作
     *
     * @return Application
     */
    private fun getApplication(): Application {
        if (application == null) {
            application = BaseApplication.getInstance()
        }
        return application!!
    }

    /**
     * 文字资源转为String类型
     *
     * @param resId 文字资源
     * @return 文字
     */
    fun convertString(@StringRes resId: Int): String {
        return getApplication().getString(resId)
    }

    /**
     * 颜色资源转为颜色数值
     *
     * @param resId 颜色资源
     * @return 颜色数值
     */
    fun convertColor(@ColorRes resId: Int): Int {
        return getApplication().resources.getColor(resId)
    }

    /**
     * 颜色资源转为颜色数值
     *
     * @param resId 颜色资源
     * @return 颜色数值
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun convertDrawable(@DrawableRes resId: Int): Drawable {
        return getApplication().resources.getDrawable(resId)
    }

}