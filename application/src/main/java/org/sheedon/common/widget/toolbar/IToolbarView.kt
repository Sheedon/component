package org.sheedon.common.widget.toolbar

import android.view.ViewGroup
import org.sheedon.common.data.model.IToolbarModel

/**
 * 标题栏提供职责
 * 必要提供方法：
 * 1. 加载标题栏View
 * 2. 设置标题栏配置信息
 * 3. 销毁动作
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/11 22:02
 */
interface IToolbarView {

    /**
     * 加载标题栏View
     * 实际上为当前IToolbarView的实例
     */
    fun loadToolbarView(): ViewGroup

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