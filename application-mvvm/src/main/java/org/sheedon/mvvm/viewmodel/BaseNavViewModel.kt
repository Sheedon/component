package org.sheedon.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import org.sheedon.lifecycle.LifecycleManager
import org.sheedon.mvvm.viewmodel.actuator.Actuator
import org.sheedon.mvvm.viewmodel.actuator.ActuatorProvider
import org.sheedon.mvvm.viewmodel.livedata.UnPeekLiveData

/**
 * 内部包含Fragment的Activity，且内部Fragment也需要持有该ViewModel而使用
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 1:59 下午
 */
abstract class BaseNavViewModel : ViewModel(){

    // 消息发送端
    private val showLoading = UnPeekLiveData<String>()
    fun getShowLoading(): UnPeekLiveData<String> = showLoading

    // 消息发送端
    private val messageEmitter = UnPeekLiveData<String>()
    fun getMessageEmitter(): UnPeekLiveData<String> = messageEmitter

    // 执行动作发送器
    private val handleAction = UnPeekLiveData<Int>()
    fun getHandleAction(): UnPeekLiveData<Int> = handleAction

    // 执行器提供者
    private var actuatorProvider: ActuatorProvider? = null

    private val lifecycleManager = LifecycleManager()
    protected fun loadLifecycleManager() = lifecycleManager

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
        lifecycleManager.onDestroy()
    }
}