package org.sheedon.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    // 消息发送端
    private val messageEmitter = MutableLiveData<String>()
    fun getMessageEmitter(): MutableLiveData<String> = messageEmitter

    // 执行动作发送器
    private val handleAction = MutableLiveData<Int>()
    fun getHandleAction(): MutableLiveData<Int> = handleAction

    // 执行器提供者
    private var actuatorProvider: ActuatorProvider? = null

    /**
     * 初始化创建数据分发执行器
     */
    abstract fun initActuators()

    /**
     * 创建数据分发执行器
     *
     * @param modelClass 执行器类
     * @param <T>        Actuator
     * @return Actuator
     */
    protected open fun <T : Actuator> getActuator(modelClass: Class<T>): T {
        if (actuatorProvider == null) {
            actuatorProvider = create()
        }
        return actuatorProvider!!.get(modelClass)
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
    }

}