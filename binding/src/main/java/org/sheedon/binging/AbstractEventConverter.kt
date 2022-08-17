package org.sheedon.binging

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import kotlin.jvm.Throws

/**
 * 绑定配置项转化器
 *
 * 1. 整体结构由【XML】绑定固定事件（Event），转化为DataBindingConfig，由调度对象设置到binding中。
 * 2. 向外提供额外方法调度对象，用于使用事件的部分调度。
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/8 17:59
 */
abstract class AbstractEventConverter<Handler : EventHandler> {

    private var activity: ComponentActivity? = null
    private var fragment: Fragment? = null
    private var callback: EventCallback? = null
    protected val event: AbstractEvent<out EventCallback> by lazy {
        when {
            activity != null -> {
                createRealEvent(activity!!, callback)
            }
            fragment != null -> {
                createRealEvent(fragment!!, callback)
            }
            else -> {
                createRealEvent(callback)
            }
        }
    }

    /**
     * 注入Activity
     *
     * @param activity Activity
     * */
    fun inject(activity: ComponentActivity) {
        this.activity = activity
    }

    /**
     * 注入Fragment
     *
     * @param fragment Fragment
     * */
    fun inject(fragment: Fragment) {
        this.fragment = fragment
    }

    /**
     * 注入反馈监听
     *
     * @param callback Callback
     * */
    fun <Callback : EventCallback> inject(callback: Callback) {
        this.callback = callback
    }

    /**
     * 创建真实的事件
     * */
    @Throws(RuntimeException::class)
    protected open fun createRealEvent(
        activity: ComponentActivity,
        callback: EventCallback?
    ): AbstractEvent<out EventCallback> {
        throw RuntimeException("Not yet implemented!")
    }

    /**
     * 创建真实的事件
     * */
    @Throws(RuntimeException::class)
    protected open fun createRealEvent(
        fragment: Fragment,
        callback: EventCallback?
    ): AbstractEvent<out EventCallback> {
        throw RuntimeException("Not yet implemented!")
    }

    /**
     * 创建真实的事件
     * */
    @Throws(RuntimeException::class)
    protected open fun createRealEvent(
        callback: EventCallback?
    ): AbstractEvent<out EventCallback> {
        throw RuntimeException("Not yet implemented!")
    }

    /**
     * 转化为【数据绑定配置参数的格式】
     * 以BR.id 为键，实现方法/对象为值的绑定配置组
     * */
    fun convertDataBindingConfig(): DataBindingConfig {
        return event.convertDataBindingConfig()
    }

    /**
     * 加载事件的方法对象
     * */
    abstract fun loadHandler(): Handler?

    /**
     * 销毁事件
     * */
    fun destroy() {
        activity = null
        fragment = null
        callback = null
        event.destroy()
    }
}