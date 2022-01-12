package org.sheedon.mvvm.viewmodel.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的Short类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 1:38 下午
 */
class ShortLiveData : MutableLiveData<Short>() {

    override fun setValue(value: Short) {
        super.setValue(value)
    }

    override fun getValue(): Short {
        return super.getValue() ?: 0
    }
}