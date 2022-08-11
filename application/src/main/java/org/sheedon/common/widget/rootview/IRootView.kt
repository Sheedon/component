package org.sheedon.common.widget.rootview

import android.view.ViewGroup
import android.widget.FrameLayout
import org.sheedon.common.data.model.IToolbarModel

/**
 * 根布局职责
 *
 * 必要提供方法：
 * 1. 获取子布局承载者
 * 2. 设置标题栏配置信息
 * 3. 销毁动作
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/11 22:48
 */
interface IRootView {

    /**
     * 获取根布局
     */
    fun getRootView(): ViewGroup

    /**
     * 获取承载子布局的承载Layout
     */
    fun getChildLayout(): FrameLayout

    /**
     * 设置标题栏中配置信息
     * 返回/标题栏/菜单键
     */
    fun setToolbarModel(model: IToolbarModel)

    /**
     * 销毁动作
     */
    fun destroy()
}