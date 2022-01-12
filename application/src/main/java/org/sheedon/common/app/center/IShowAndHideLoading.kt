package org.sheedon.common.app.center

import androidx.annotation.StringRes

/**
 * 加载框职责
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 10:41 上午
 */
interface IShowAndHideLoading {

    /**
     * 显示加载框信息
     * @param res 描述信息
     */
    fun showLoading(@StringRes res: Int)

    /**
     * 显示加载框信息
     * @param message 描述信息
     */
    fun showLoading(message: String = "")

    /**
     * 隐藏加载框
     */
    fun hideLoading()
}