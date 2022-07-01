package org.sheedon.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import org.sheedon.lifecycle.LifecycleManager
import org.sheedon.mvvm.event.SignalNotify
import org.sheedon.mvvm.viewmodel.actuator.Actuator
import org.sheedon.mvvm.viewmodel.actuator.ActuatorProvider

/**
 * 基础ViewModel
 * 包含错误消息message 和 执行动作handleAction
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 12:30 下午
 */
abstract class BaseViewModel : ViewModel() {

    // 信号通知对象，用于通知「显示弹窗」、「隐藏弹窗」、「提示错误描述」、「处理动作」
    private val notifier = SignalNotify()
    fun getNotifier(): SignalNotify = notifier

    // 执行器提供者
    private var actuatorProvider: ActuatorProvider? = null

    private val lifecycleManager = LifecycleManager()
    protected fun loadLifecycleManager() = lifecycleManager


    /**
     * 初始化创建数据分发执行器
     */
    open fun initActuators() {

    }

    /**
     * 初始化数据
     */
    open fun initData() {

    }

    /**
     * 创建数据分发执行器
     *
     * @param modelClass 执行器类
     * @param <T>        Actuator
     * @return Actuator
     */
    protected open fun <T : Actuator> getActuator(modelClass: Class<T>, vararg value: Any?): T {
        if (actuatorProvider == null) {
            actuatorProvider = create()
        }
        val values: Array<*> = value.let {
            if (value.isNullOrEmpty()) {
                loadActuatorParameter() ?: emptyArray()
            } else {
                value
            }
        }
        return actuatorProvider!!.get(modelClass, values)
    }

    protected open fun loadActuatorParameter(): Array<*>? {
        return null
    }

    /**
     * 创建「数据分发执行器」的持有者类
     *
     * @return ActuatorProvider
     */
    protected open fun create(): ActuatorProvider {
        return ActuatorProvider()
    }

    /**
     * 发送加载框
     */
    @JvmOverloads
    protected fun sendShowLoading(msg: String = "") {
        notifier.showLoading(msg)
    }

    /**
     * 隐藏弹窗
     */
    protected fun sendHideLoading(){
        notifier.dismissDialog()
    }

    /**
     * 发送处理动作
     */
    protected fun sendHandleAction(action: Int) {
        notifier.sendHandleAction(action)
    }

    /**
     * 发送提示消息
     */
    protected fun sendMessage(msg: String?) {
        notifier.sendMessage(msg)
    }

    /**
     * 清空销毁
     */
    override fun onCleared() {
        super.onCleared()
        actuatorProvider?.clear()
        actuatorProvider = null
        lifecycleManager.onDestroy()
    }

}