package org.sheedon.tool.utils

import java.lang.ref.WeakReference

/**
 * 数据传输工具类
 * 防止采用Intent传输数据时，超过1MB限制的异常场景
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/9/2 14:01
 */
object IntentDataHolderUtil {

    private val dataMap = HashMap<String, WeakReference<Any>>()

    fun setData(key: String, any: Any) {
        val value = WeakReference(any)
        dataMap[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key: String): T? {
        val data = dataMap[key]
        return try {
            data?.get() as? T
        } catch (e: Exception) {
            null
        }
    }

}