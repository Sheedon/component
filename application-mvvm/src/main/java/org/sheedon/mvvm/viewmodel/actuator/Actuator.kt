package org.sheedon.mvvm.viewmodel.actuator

/**
 * 基础执行器，将数据源发送到XML中和双向绑定
 * 由 ViewModel 持有，viewModel得到数据后，借助该类和XML通信
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/6 12:33 下午
 */
open class Actuator {

    init {
        initConfig()
    }

    protected open fun initConfig() {

    }

    open fun onCleared() {

    }
}