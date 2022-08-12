package org.sheedon.common.data.build

import android.content.Context
import org.sheedon.common.widget.rootview.IRootView
import org.sheedon.common.widget.rootview.NormalRootView
import org.sheedon.common.widget.toolbar.IToolbarView
import org.sheedon.common.widget.toolbar.NormalToolbarView

/**
 * 根布局和标题栏的构造工程类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/12 15:15
 */
open class RootViewBuildFactory {

    /**
     * 构建根布局
     * */
    open fun buildRootView(context: Context, toolbar: IToolbarView): IRootView {
        return NormalRootView(context, toolbar = toolbar)
    }

    /**
     * 构建标题栏布局
     * */
    open fun buildToolbarView(context: Context): IToolbarView {
        return NormalToolbarView(context)
    }
}