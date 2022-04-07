package org.sheedon.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import org.sheedon.lifecycle.LifecycleManager
import org.sheedon.mvvm.viewmodel.actuator.Actuator
import org.sheedon.mvvm.viewmodel.actuator.ActuatorProvider
import org.sheedon.mvvm.viewmodel.livedata.IntLiveData
import org.sheedon.mvvm.viewmodel.livedata.StringLiveData

/**
 * 基础ViewModel
 * 包含错误消息message 和 执行动作handleAction
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 12:30 下午
 */
abstract class BaseViewModel : ViewModel() {

    // 显示Loading
    private val showLoading = StringLiveData()
    fun getShowLoading(): StringLiveData = showLoading

    // 消息发送端
    private val messageEmitter = StringLiveData()
    fun getMessageEmitter(): StringLiveData = messageEmitter

    // 执行动作发送器
    private val handleAction = IntLiveData()
    fun getHandleAction(): IntLiveData = handleAction

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
     * 清空销毁
     */
    override fun onCleared() {
        super.onCleared()
        actuatorProvider?.clear()
        actuatorProvider = null
        lifecycleManager.onDestroy()
    }

}