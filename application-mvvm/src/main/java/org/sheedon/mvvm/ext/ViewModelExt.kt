package org.sheedon.mvvm.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import org.sheedon.common.handler.ToastHandler
import org.sheedon.common.handler.ViewModelProviderHandler
import org.sheedon.mvvm.event.notify.NotifyStatus
import org.sheedon.mvvm.ui.activities.AbstractModuleActivity
import org.sheedon.mvvm.ui.fragments.AbstractModuleFragment
import org.sheedon.mvvm.viewmodel.AbstractViewModel
import org.sheedon.mvvm.viewmodel.BaseViewModel
import org.sheedon.mvvm.viewmodel.livedata.ProtectedUnPeekLiveData
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
inline fun <VM : AbstractViewModel> AbstractModuleActivity<out VM>.registerObserver(
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

inline fun <VM : AbstractViewModel> AbstractModuleActivity<out VM>.sendSignalNotify(
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
inline fun <VM : AbstractViewModel> AbstractModuleFragment<out VM>.registerObserver(
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

inline fun <VM : AbstractViewModel> AbstractModuleFragment<out VM>.sendSignalNotify(
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
inline fun <reified VM : BaseViewModel> AppCompatActivity.getAppViewModel(): VM {
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
inline fun <reified VM : BaseViewModel> Fragment.getAppViewModel(): VM {
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








