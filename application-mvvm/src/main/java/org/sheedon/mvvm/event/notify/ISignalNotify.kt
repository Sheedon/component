package org.sheedon.mvvm.event.notify

import androidx.lifecycle.LiveData

/**
 * 通知信号，用于通知开启弹窗/关闭弹窗/发送事件信号/错误消息信号
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/7/1 13:04
 */
interface ISignalNotify {


    fun getNotifyStatus():LiveData<NotifyStatus>

    /**
     * 显示弹窗
     *
     * @param message 弹窗描述信息
     */
    fun showLoading(message: String = "")

    /**
     * 隐藏弹窗
     *
     * @param message 弹窗描述信息
     */
    fun dismiss()

    /**
     * 发送一个toast
     */
    fun sendDataError(message: String? = null)

    /**
     * 发送一个执行的动作
     */
    fun sendSignalAction(action: Int)
}