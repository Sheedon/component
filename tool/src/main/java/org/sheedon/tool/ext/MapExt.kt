package org.sheedon.tool.ext

import java.util.AbstractMap

/**
 * Map扩展类
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/10/8 09:39
 */

/**
 * map 指定key的value数据是否为空，若为空，则通过[remappingFunction]创建值，并且将值返回。
 */
fun <K, V> AbstractMap<K, V>.computeIfBuild(
    key: K,
    remappingFunction: () -> V
): V {
    var value = this[key]
    if (value == null) {
        value = remappingFunction()
        this[key] = value
    }
    return value!!
}