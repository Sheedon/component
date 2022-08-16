package org.sheedon.mvvm.lifecycle.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的 Double 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 1:34 下午
 */
class DoubleLiveData : MutableLiveData<Double>() {
    override fun setValue(value: Double) {
        super.setValue(value)
    }

    override fun getValue(): Double {
        return super.getValue() ?: 0.0
    }
}