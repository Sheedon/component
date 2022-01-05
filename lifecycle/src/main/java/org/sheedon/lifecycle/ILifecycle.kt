package org.sheedon.lifecycle

/**
 * 生命周期接口
 * 注意承载销毁的职责
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 11:26 上午
 */
interface ILifecycle {

    fun onDestroy()
}