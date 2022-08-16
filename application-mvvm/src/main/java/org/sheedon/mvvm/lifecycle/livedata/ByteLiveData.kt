package org.sheedon.mvvm.lifecycle.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的 byte 类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 1:32 下午
 */
class ByteLiveData : MutableLiveData<Byte>() {

    override fun setValue(value: Byte) {
        super.setValue(value)
    }

    override fun getValue(): Byte {
        return super.getValue() ?: 0
    }

}