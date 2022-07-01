package org.sheedon.mvvm.event

import androidx.lifecycle.MutableLiveData
import org.sheedon.mvvm.viewmodel.livedata.IntLiveData
import org.sheedon.mvvm.viewmodel.livedata.StringLiveData
import org.sheedon.mvvm.viewmodel.livedata.UnPeekLiveData

/**
 * 信号通知执行类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/6/23 15:54
 */
open class SignalNotify {

    // 显示Loading notifier
    private val showLoading = StringLiveData()
    fun getShowLoading(): StringLiveData = showLoading

    // 隐藏Loading
    private val dismissDialog = MutableLiveData<Unit>()
    fun getDismissLoading(): MutableLiveData<Unit> = dismissDialog

    // 消息发送端
    private val messageEmitter = StringLiveData()
    fun getMessageEmitter(): StringLiveData = messageEmitter

    // 执行动作发送器
    private val handleAction = IntLiveData()
    fun getHandleAction(): IntLiveData = handleAction

    /**
     * 显示弹窗
     *
     * @param message 弹窗描述信息
     */
    open fun showLoading(message: String = "") {
        showLoading.postValue(message)
    }

    /**
     * 隐藏弹窗
     *
     * @param message 弹窗描述信息
     */
    open fun dismissDialog() {
        dismissDialog.postValue(Unit)
    }

    /**
     * 发送一个toast
     */
    open fun sendMessage(message: String? = null) {
        dismissDialog()
        messageEmitter.postValue(message ?: "")
    }

    /**
     * 发送一个执行的动作
     */
    open fun sendHandleAction(action: Int) {
        dismissDialog()
        handleAction.postValue(action)
    }

}

open class SignalNavNotify {

    // 显示Loading notifier
    private val showLoading = UnPeekLiveData<String>()
    fun getShowLoading(): UnPeekLiveData<String> = showLoading

    // 隐藏Loading
    private val dismissDialog = UnPeekLiveData<Unit>()
    fun getDismissLoading(): UnPeekLiveData<Unit> = dismissDialog

    // 消息发送端
    private val messageEmitter = UnPeekLiveData<String>()
    fun getMessageEmitter(): UnPeekLiveData<String> = messageEmitter

    // 执行动作发送器
    private val handleAction = UnPeekLiveData<Int>()
    fun getHandleAction(): UnPeekLiveData<Int> = handleAction

    /**
     * 显示弹窗
     *
     * @param message 弹窗描述信息
     */
    open fun showLoading(message: String = "") {
        showLoading.postValue(message)
    }

    /**
     * 隐藏弹窗
     *
     * @param message 弹窗描述信息
     */
    open fun dismissDialog() {
        dismissDialog.postValue(Unit)
    }

    /**
     * 发送一个toast
     */
    open fun sendMessage(message: String? = null) {
        dismissDialog()
        messageEmitter.postValue(message ?: "")
    }

    /**
     * 发送一个执行的动作
     */
    open fun sendHandleAction(action: Int) {
        dismissDialog()
        handleAction.postValue(action)
    }

}