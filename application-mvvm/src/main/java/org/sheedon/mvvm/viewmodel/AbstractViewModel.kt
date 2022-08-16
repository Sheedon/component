package org.sheedon.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import org.sheedon.lifecycle.LifecycleManager
import org.sheedon.mvvm.event.notify.ISignalNotify
import org.sheedon.mvvm.event.notify.SignalNavNotify
import org.sheedon.mvvm.event.notify.SignalNotify
import org.sheedon.mvvm.viewmodel.actuator.Actuator
import org.sheedon.mvvm.viewmodel.actuator.ActuatorProvider

/**
 * 基础模块化的ViewModel
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/7/1 14:12
 */
abstract class AbstractViewModel : ViewModel() {

    // 信号通知对象，用于通知「显示弹窗」、「隐藏弹窗」、「提示错误描述」、「处理动作」
    // 默认取子类创建的loadNotifier()，若未创建则加载默认的loadDefaultNotifier()方法
    protected val signalNotify: ISignalNotify by lazy {
        loadNotifier() ?: loadDefaultNotifier()
    }

    fun getNotifier(): ISignalNotify = signalNotify

    // 执行器提供者
    private val actuatorProvider: ActuatorProvider by lazy { createActuatorProvider() }

    // 生命周期管理者
    private val lifecycleManager = LifecycleManager()
    protected fun loadLifecycleManager() = lifecycleManager


    /**
     * 初始化创建数据分发执行器
     */
    open fun initActuators() {

    }


    /**
     * 创建「数据分发执行器」的持有者类
     *
     * @return ActuatorProvider
     */
    protected open fun createActuatorProvider(): ActuatorProvider {
        return ActuatorProvider()
    }

    /**
     * 创建数据分发执行器
     *
     * @param modelClass 执行器类
     * @param <T>        Actuator
     * @return Actuator
     */
    protected open fun <T : Actuator> getActuator(modelClass: Class<T>, vararg value: Any?): T {
        val values: Array<*> = value.let {
            if (value.isNullOrEmpty()) {
                loadActuatorParameter() ?: emptyArray()
            } else {
                value
            }
        }
        return actuatorProvider.get(modelClass, values)
    }

    /**
     * 加载分发参数集合
     */
    protected open fun loadActuatorParameter(): Array<*>? = null

    /**
     * 初始化数据
     */
    open fun initData() {

    }

    /**
     * 加载通知执行者，由子类自定义创建
     * 用于执行显示隐藏弹窗、事件通知 和 错误消息通知。
     */
    protected open fun loadNotifier(): ISignalNotify? = null

    /**
     * 加载默认的通知者，
     * 根据[enableUnPeek]是否启动不可见的通知，来加载[SignalNavNotify]或[SignalNotify]
     */
    private fun loadDefaultNotifier(): ISignalNotify {
        return if (enableUnPeek()) {
            SignalNavNotify()
        } else {
            SignalNotify()
        }
    }

    /**
     * 是否启动不可见的通知，防止产生数据回流的方法
     * 控制[ISignalNotify]实现方法
     */
    protected open fun enableUnPeek(): Boolean = false

    /**
     * 发送加载框
     */
    @JvmOverloads
    protected fun sendShowLoading(msg: String = "") {
        signalNotify.showLoading(msg)
    }

    /**
     * 隐藏弹窗
     */
    protected fun sendHideLoading() {
        signalNotify.dismiss()
    }

    /**
     * 发送处理动作
     */
    protected fun sendHandleAction(action: Int) {
        signalNotify.sendSignalAction(action)
    }

    /**
     * 发送提示消息
     */
    protected fun sendMessage(msg: String?) {
        signalNotify.sendDataError(msg)
    }

    /**
     * 清空销毁
     */
    override fun onCleared() {
        super.onCleared()
        actuatorProvider.clear()
        lifecycleManager.onDestroy()
    }


}