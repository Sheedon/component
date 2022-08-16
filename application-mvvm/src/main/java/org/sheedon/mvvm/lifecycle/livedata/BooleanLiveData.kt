package org.sheedon.mvvm.lifecycle.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的 Boolean 类型 MutableLiveData 提供类默认值，避免取值还需要判空
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 1:30 下午
 */
class BooleanLiveData : MutableLiveData<Boolean>() {

    override fun setValue(value: Boolean) {
        super.setValue(value)
    }

    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}