package org.sheedon.tool.ext

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/8/24 23:14
 */

fun Any.convertString(): String {
    if (this is String) {
        return this
    }

    return toString()
}