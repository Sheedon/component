package org.sheedon.mvvm.event.notify

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import org.sheedon.mvvm.lifecycle.livedata.UnPeekLiveData

/**
 * 默认的信号通知实现类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/7/1 13:09
 */
open class SignalNotify : ISignalNotify {

    // 通知消息发射者
    private val notifyEmitter = MutableLiveData<NotifyStatus>()
    override fun getNotifyStatus(): MutableLiveData<NotifyStatus> = notifyEmitter

    /**
     * 显示弹窗
     *
     * @param message 弹窗描述信息
     */
    override fun showLoading(message: String) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.Loading(message)
        } else {
            notifyEmitter.postValue(NotifyStatus.Loading(message))
        }
    }

    /**
     * 隐藏弹窗
     *
     * @param message 弹窗描述信息
     */
    override fun dismiss() {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.Dismiss()
        } else {
            notifyEmitter.postValue(NotifyStatus.Dismiss())
        }
    }

    /**
     * 发送一个toast
     */
    override fun sendDataError(message: String?) {
        dismiss()
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.DataError(message ?: "")
        } else {
            notifyEmitter.postValue(NotifyStatus.DataError(message ?: ""))
        }
    }

    /**
     * 发送一个执行的动作
     */
    override fun sendSignalAction(action: Int) {
        dismiss()
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.SignalAction(action)
        } else {
            notifyEmitter.postValue(NotifyStatus.SignalAction(action))
        }
    }

}

open class SignalNavNotify : ISignalNotify {

    // 通知消息发射者
    private val notifyEmitter = UnPeekLiveData<NotifyStatus>()
    override fun getNotifyStatus(): UnPeekLiveData<NotifyStatus> = notifyEmitter

    /**
     * 显示弹窗
     *
     * @param message 弹窗描述信息
     */
    override fun showLoading(message: String) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.Loading(message)
        } else {
            notifyEmitter.postValue(NotifyStatus.Loading(message))
        }
    }

    /**
     * 隐藏弹窗
     *
     * @param message 弹窗描述信息
     */
    override fun dismiss() {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.Dismiss()
        } else {
            notifyEmitter.postValue(NotifyStatus.Dismiss())
        }
    }

    /**
     * 发送一个toast
     */
    override fun sendDataError(message: String?) {
        dismiss()
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.DataError(message ?: "")
        } else {
            notifyEmitter.postValue(NotifyStatus.DataError(message ?: ""))
        }
    }

    /**
     * 发送一个执行的动作
     */
    override fun sendSignalAction(action: Int) {
        dismiss()
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            notifyEmitter.value = NotifyStatus.SignalAction(action)
        } else {
            notifyEmitter.postValue(NotifyStatus.SignalAction(action))
        }
    }

}