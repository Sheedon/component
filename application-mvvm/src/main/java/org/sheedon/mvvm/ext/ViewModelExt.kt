package org.sheedon.mvvm.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.sheedon.common.handler.ToastHandler
import org.sheedon.common.handler.ViewModelProviderHandler
import org.sheedon.mvvm.event.notify.NotifyStatus
import org.sheedon.mvvm.viewmodel.AbstractViewModel
import org.sheedon.mvvm.lifecycle.livedata.ProtectedUnPeekLiveData
import org.sheedon.mvvm.ui.activities.AbstractVMActivity
import org.sheedon.mvvm.ui.fragments.AbstractVMFragment
import org.sheedon.tool.ext.checkValue
import java.lang.reflect.ParameterizedType


/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

/**
 * 在Activity中注册【显示隐藏弹窗】、【发送信号】、【发送错误消息】的观察者
 */
inline fun <VM : AbstractViewModel> AbstractVMActivity<out VM>.registerObserver(
    mState: VM,
    crossinline actionHandler: (status: Int) -> Unit
) {

    val notifier = mState.getNotifier()

    // 弹窗显示监听
    val notifyStatus = notifier.getNotifyStatus()

    if (notifyStatus is ProtectedUnPeekLiveData) {
        notifyStatus.observeInActivity(this,
            Observer {
                sendSignalNotify(it, actionHandler)
            })
    } else {
        notifyStatus.observe(this) {
            sendSignalNotify(it, actionHandler)
        }
    }
}

inline fun <VM : AbstractViewModel> AbstractVMActivity<out VM>.sendSignalNotify(
    status: NotifyStatus,
    crossinline actionHandler: (status: Int) -> Unit
) {
    when (status) {
        is NotifyStatus.Loading -> showLoading(message = status.message)
        is NotifyStatus.Dismiss -> hideLoading()
        is NotifyStatus.DataError -> {
            status.message.isEmpty().checkValue {
                ToastHandler.showToast(status.message)
            }
        }
        is NotifyStatus.SignalAction -> status.code?.let {
            actionHandler(it)
        }
    }
}

/**
 * 注册【显示隐藏弹窗】、【发送信号】、【发送错误消息】的观察者
 */
inline fun <VM : AbstractViewModel> AbstractVMFragment<out VM>.registerObserver(
    mState: VM,
    crossinline actionHandler: (status: Int) -> Unit
) {

    val notifier = mState.getNotifier()

    // 弹窗显示监听
    val notifyStatus = notifier.getNotifyStatus()

    if (notifyStatus is ProtectedUnPeekLiveData) {
        notifyStatus.observeInFragment(this,
            Observer {
                sendSignalNotify(it, actionHandler)
            })
    } else {
        notifyStatus.observe(this) {
            sendSignalNotify(it, actionHandler)
        }
    }
}

inline fun <VM : AbstractViewModel> AbstractVMFragment<out VM>.sendSignalNotify(
    status: NotifyStatus,
    crossinline actionHandler: (status: Int) -> Unit
) {
    when (status) {
        is NotifyStatus.Loading -> showLoading(message = status.message)
        is NotifyStatus.Dismiss -> hideLoading()
        is NotifyStatus.DataError -> {
            status.message.isEmpty().checkValue {
                ToastHandler.showToast(status.message)
            }
        }
        is NotifyStatus.SignalAction -> status.code?.let {
            actionHandler(it)
        }
    }
}

/**
 * 在Activity中得到Application上下文的ViewModel
 */
inline fun <reified VM : AbstractViewModel> AppCompatActivity.getAppViewModel(): VM {
    this.application.let {
        if (it == null) {
            throw NullPointerException("Your Application is null")
        } else {
            return ViewModelProviderHandler.getApplicationScopeViewModel(
                VM::class.java,
                it
            )
        }
    }
}

/**
 * 在Fragment中得到Application上下文的ViewModel
 * 提示，在fragment中调用该方法时，请在该Fragment onCreate以后调用或者请用by lazy方式懒加载初始化调用，不然会提示requireActivity没有导致错误
 */
inline fun <reified VM : AbstractViewModel> Fragment.getAppViewModel(): VM {
    (this.requireActivity().application).let {
        if (it == null) {
            throw NullPointerException("Your Application is null")
        } else {
            return ViewModelProviderHandler.getApplicationScopeViewModel(
                VM::class.java,
                it
            )
        }
    }
}








