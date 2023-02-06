package org.sheedon.common.handler

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import org.sheedon.common.data.build.RootViewBuildFactory
import org.sheedon.common.utils.BarUtils
import org.sheedon.common.widget.rootview.IRootView
import org.sheedon.common.widget.toolbar.IToolbarView

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

    private var color = Color.TRANSPARENT

    private var rootViewFactory = RootViewBuildFactory()

    companion object {
        private val INSTANCE = ConfigHandler()

        @JvmStatic
        fun getInstance() = INSTANCE

        /**
         * 获取是否是亮模式
         */
        @JvmStatic
        fun setStatusBarMode(activity: AppCompatActivity) {
            BarUtils.setStatusBarColor(activity, INSTANCE.color)
            BarUtils.setStatusBarLightMode(activity, INSTANCE.lightModel)
        }

        /**
         * 设置根布局构建工厂类
         * */
        fun setRootViewBuildFactory(factory: RootViewBuildFactory) {
            INSTANCE.rootViewFactory = factory
        }

        /**
         * 加载基础根布局
         * */
        @JvmStatic
        fun loadRootView(context: Context, toolbar: IToolbarView): IRootView =
            INSTANCE.rootViewFactory.buildRootView(context, toolbar)

        /**
         * 加载基础标题布局
         * */
        @JvmStatic
        fun loadToolbarView(context: Context): IToolbarView =
            INSTANCE.rootViewFactory.buildToolbarView(context)

        /**
         * 加载标题栏+状态栏的背景
         * */
        @JvmStatic
        internal fun loadToolbarBackground(context: Context): Drawable? =
            INSTANCE.rootViewFactory.buildToolbarBackground(context)

        /**
         * 设置App状态颜色
         */
        fun setAppStatusColor(color: Int) {
            INSTANCE.color = color
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
}