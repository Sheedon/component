package org.sheedon.mvvm.viewmodel

import org.sheedon.binging.EventCallback
import org.sheedon.binging.EventHandler

/**
 * 基础模块化的ViewModel
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/7/1 14:12
 */
abstract class AbstractModuleViewModel<Callback : EventCallback, Handler : EventHandler> : AbstractViewModel() {

    // 数据绑定处理者
    private var handler: Handler? = null
    protected fun loadHandler(): Handler? = handler

    /**
     * 加载处理的反馈Callback
     * */
    abstract fun loadCallback(): Callback

    /**
     * 注入绑定事件处理行为
     * */
    fun inject(handler: Handler) {
        this.handler = handler
    }

    /**
     * 清空销毁
     */
    override fun onCleared() {
        super.onCleared()
        handler = null
    }

}