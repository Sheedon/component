package org.sheedon.lifecycle

/**
 * 生命周期管理类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 11:37 上午
 */
class LifecycleManager : ILifecycle {

    private val elements = HashSet<ILifecycle>()

    /**
     * 追加持有生命周期的元素
     *
     * @param element 持有生命周期的元素
     */
    fun addLifecycle(element: ILifecycle) {
        elements.add(element)
    }


    @Override
    override fun onDestroy() {
        elements.forEach { it.onDestroy() }
        elements.clear()
    }
}