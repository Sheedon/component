package org.sheedon.common.handler

import android.graphics.Color
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import org.sheedon.common.R
import org.sheedon.common.data.model.Toolbar
import org.sheedon.common.databinding.LayoutToolbarBinding
import org.sheedon.common.utils.BarUtils

/**
 * 配置处理器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 11:17 下午
 */
class ConfigHandler private constructor() {

    // 是否为白天模式
    private var lightModel = true

    // toolbar资源id
    private var toolbar = Toolbar()

    companion object {
        private val INSTANCE = ConfigHandler()

        @JvmStatic
        fun getInstance() = INSTANCE

        /**
         * 获取是否是亮模式
         */
        @JvmStatic
        fun setStatusBarMode(activity: AppCompatActivity) {
            BarUtils.setStatusBarColor(activity, Color.TRANSPARENT)
            BarUtils.setStatusBarLightMode(activity, INSTANCE.lightModel)
        }

        /**
         * 获取 Toolbar ID
         */
        @JvmStatic
        fun getToolbarId() = INSTANCE.toolbar.toolbarId

        @JvmStatic
        fun setToolbar(toolbar: Toolbar) {
            INSTANCE.toolbar = toolbar
        }

    }

    /**
     * 附加是否是亮模式
     *
     * @param lightModel 是否为明亮模式
     */
    fun attachLightModel(lightModel: Boolean) {
        this.lightModel = lightModel
    }

    internal fun getFlChild(binding: ViewDataBinding) = toolbar.getFlChild(binding)
}