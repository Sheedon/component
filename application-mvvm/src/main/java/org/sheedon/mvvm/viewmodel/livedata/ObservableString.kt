package org.sheedon.mvvm.viewmodel.livedata

import androidx.databinding.ObservableField

/**
 * 自定义String类型的ObservableField，默认值为""
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/12 1:24 下午
 */
class ObservableString(value: String = "") : ObservableField<String>(value) {

    override fun set(value: String) {
        super.set(value)
    }

    override fun get(): String {
        return super.get() ?: ""
    }

}