package org.sheedon.mvvm.lifecycle.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的Float类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 1:36 下午
 */
class FloatLiveData : MutableLiveData<Float>() {
    override fun setValue(value: Float) {
        super.setValue(value)
    }

    override fun getValue(): Float {
        return super.getValue() ?: 0F
    }
}