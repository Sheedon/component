package org.sheedon.common.data.model

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData

/**
 * 导航栏功能
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 11:39 下午
 */
interface IToolbarModel {

    // 标题
    fun getTitle(): MutableLiveData<Int>

    // 菜单标题
    fun getMenuTitle(): ObservableField<String>

    // 菜单是否显示
    fun getMenuVisibility(): ObservableInt

    // 返回是否显示
    fun getBackVisibility(): Int

    // 设置标题
    fun setTitle(title: Int)

    // 返回按钮触发
    fun onBackClick()

    // 菜单按钮触发
    fun onMenuClick()
}