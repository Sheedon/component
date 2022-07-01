package org.sheedon.mvvm.data

import android.util.SparseArray
import androidx.lifecycle.Lifecycle

/**
 * 旨在于将页面事件转化为对应策略，已减少在Activity/Fragment中编写过量重复代码，或者继承对应职责的Activity/Fragment
 * 出现重复编写。
 *
 * 例如：A-Activity需要有「扫码功能」，B-Activity需要有「列表功能」，C-Activity需要有「扫码+列表」。
 * 那么如果我们建立来一个扫码的父Activity，又有个列表的父Activity，现在扫码+列表，会不会出现我们要编写一个继承「扫码Activity」
 * 或者「列表Activity」的Activity，再实现一个另一项功能呢。
 * 因此继承的方案不适合我们在这里使用，我们需要转化为组合的思路。
 * Activity中创建一个调用事件注册的方法，然后我们配置多个功能策略（扫码/列表等等），那么Activity中我们就不需要过多的
 * 去继承功能父类。
 *
 * 当前事件注册存在两个职责：
 * 1.将当前事件职责与XML对应事件绑定。例如，构建一个列表的适配器：ID：BR.adapter - Value：XXAdapter()
 * 2.补充事件调度的生命周期。例如扫码功能采用物理按钮扫码，那么Activity/Fragment需要在onResume注册，在onPause中取消，
 * 所以，我们需要对此实现个生命周期，以满足对应控件的使用。
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/6/23 13:15
 */
interface IEventRegistry {

    /**
     * 创建事件什么周期执行行为
     */
    fun createEventLifeCycle(lifecycle: Lifecycle)

    /**
     * 获取与XML绑定的参数
     */
    fun getBindingParams(): SparseArray<Any>
}