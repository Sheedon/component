package org.sheedon.binging

import android.util.SparseArray
import androidx.core.util.forEach

/**
 * tip:
 * 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
 * 通过这样的方式，来彻底解决 视图调用的一致性问题，
 * 如此，视图实例的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
 * 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。
 *
 * @Author: sheedon by KunMinX
 * @Email: sheedonsun@163.com
 * @Date: 2022/1/5 9:56 下午
 */
class DataBindingConfig {

    private val bindingParams = SparseArray<Any>()

    fun getBindingParams(): SparseArray<Any> {
        return bindingParams
    }

    fun addBindingParam(variableId: Int, params: Any?): DataBindingConfig {
        bindingParams.put(variableId, params)
        return this
    }

    fun addBindingParams(bindingParams: SparseArray<Any>): DataBindingConfig {
        bindingParams.forEach { variableId, params ->
            addBindingParam(variableId, params)
        }
        return this
    }
}