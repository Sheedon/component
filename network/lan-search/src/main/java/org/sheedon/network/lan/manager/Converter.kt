package org.sheedon.network.lan.manager

import android.content.Context

/**
 * 数据转化器
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/11/4 10:58
 */
interface Converter<T, F> {

    fun convert(data: T): F


    open class Factory {

        /**
         * 创建制造商转换器
         */
        open fun createManufactureConverter(context: Context): Converter<String, String> {
            return DefaultManufactureConverter(context)
        }
    }
}