package org.sheedon.mvvm.data

import androidx.lifecycle.Lifecycle
import kotlin.collections.HashSet

/**
 * 用于自定义配置数据绑定的参数配置项，配置信息分为两类，分别是「绑定XML的事件」和「生命周期调度」。
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/6/23 11:24
 */
open class DataBindingConfigStore(
    private val layoutId: Int = LAYOUT_ID,
    vararg eventRegistry: IEventRegistry
) {

    // 事件注册者集合
    private val eventRegistries = HashSet<IEventRegistry>()

    init {
        eventRegistry.forEach { eventRegistries.add(it) }
    }

    /**
     * 创建事件什么周期执行行为
     */
    open fun createEventLifeCycle(lifecycle: Lifecycle) {
        for (eventRegistry in eventRegistries) {
            eventRegistry.createEventLifeCycle(lifecycle)
        }
    }

    /**
     * 额外添加事件注册内容
     *
     * @param eventRegistry 事件注册对象
     * @return DataBindingConfigStore 数据配置仓库
     */
    open fun addEventRegistries(eventRegistry: IEventRegistry): DataBindingConfigStore {
        eventRegistries.add(eventRegistry)
        return this
    }

    /**
     * 获取已配置的事件注册对象
     */
    fun getEventRegistries(): List<IEventRegistry> {
        return eventRegistries.toList()
    }


    /**
     * 加载视图资源
     */
    open fun loadLayoutId() = layoutId

    companion object {

        // 布局资源ID
        const val LAYOUT_ID = -1
    }


}