package org.sheedon.binging

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import java.util.*

/**
 * 事件组合类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/12 17:52
 */
abstract class AbstractEventComponent<Callback : EventCallback,
        Handler : EventHandler> {

    /**
     * 事件集合
     * */
    private val events: LinkedList<AbstractEventConverter<out EventHandler>> by lazy {
        loadEventArray()
    }

    protected abstract fun loadEventArray(): LinkedList<AbstractEventConverter<out EventHandler>>


    /**
     * 注入Activity
     *
     * @param activity Activity
     * */
    open fun inject(activity: ComponentActivity) {
        events.forEach {
            it.inject(activity)
        }
    }

    /**
     * 注入Fragment
     *
     * @param fragment Fragment
     * */
    open fun inject(fragment: Fragment) {
        events.forEach {
            it.inject(fragment)
        }
    }

    /**
     * 注入反馈监听
     *
     * @param callback Callback
     * */
    open fun inject(callback: Callback) {
        events.forEach {
            it.inject(callback)
        }
    }

    /**
     * 提供调度者
     * */
    fun provideHandler(): Handler {
        val handlerArray = events.mapNotNull {
            it.loadHandler()
        }.toTypedArray()

        return createRealHandler(handlerArray)
    }

    /**
     * 创建真实的调度者
     * */
    protected abstract fun createRealHandler(handlerArray: Array<out EventHandler>): Handler

    /**
     * 加载数据绑定配置项
     */
    fun loadDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig().apply {
            events.forEach {
                val config = it.convertDataBindingConfig()
                addBindingParams(config.getBindingParams())
            }
        }
    }

    /**
     * 销毁
     * */
    open fun destroy() {
        events.forEach {
            it.destroy()
        }
    }
}