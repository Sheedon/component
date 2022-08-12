package org.sheedon.binging

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
interface BindingConfigConverter<Handler : EventHandler> {

    /**
     * 转化为【数据绑定配置参数的格式】
     * 以BR.id 为键，实现方法/对象为值的绑定配置组
     * */
    fun convertDataBindingConfig(): DataBindingConfig


    /**
     * 加载事件的方法对象
     * */
    fun loadProvider(): Handler?
}