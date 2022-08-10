package org.sheedon.mvvm.event.notify

/**
 * 通知状态
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/10 21:14
 */
sealed class NotifyStatus(
    val code: Int? = null,
    val message: String = ""
) {

    // 显示弹窗
    class Loading(message: String = "") : NotifyStatus(message = message)

    // 隐藏弹窗
    class Dismiss : NotifyStatus()

    // 错误消息
    class DataError(errorMessage: String = "") : NotifyStatus(message = errorMessage)

    // 通知消息
    class SignalAction(signalCode: Int? = null) : NotifyStatus(signalCode)
}